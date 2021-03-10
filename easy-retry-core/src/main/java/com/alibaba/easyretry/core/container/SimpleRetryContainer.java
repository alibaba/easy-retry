package com.alibaba.easyretry.core.container;

import com.alibaba.easyretry.common.RetryConfiguration;
import com.alibaba.easyretry.common.RetryContainer;
import com.alibaba.easyretry.common.RetryContext;
import com.alibaba.easyretry.common.RetryContext.RetryContextBuilder;
import com.alibaba.easyretry.common.RetryExecutor;
import com.alibaba.easyretry.common.constant.enums.HandleResultEnum;
import com.alibaba.easyretry.common.entity.RetryTask;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author Created by wuhao on 2020/11/5.
 */
@Slf4j
public class SimpleRetryContainer implements RetryContainer {

	private static final Integer MAX_QUEUE_SIZE = 2000;

	private RetryConfiguration retryConfiguration;

	private String namespace;

	private RetryExecutor retryExecutor;

	public SimpleRetryContainer() {
	}

	public SimpleRetryContainer(
		RetryConfiguration retryConfiguration, String namespace, RetryExecutor retryExecutor) {
		this.namespace = namespace;
		this.retryConfiguration = retryConfiguration;
		this.retryExecutor = retryExecutor;
	}

	@Override
	public void start() {
		BlockingQueue<RetryContext> queue = new PriorityBlockingQueue<>(MAX_QUEUE_SIZE);
		ThreadPoolExecutor retryExecutor =
			new ThreadPoolExecutor(
				1,
				1,
				0L,
				TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>(),
				r -> new Thread(r, "retryExecutor-Thread"));
		retryExecutor.execute(new TaskConsumer(queue));
		ThreadPoolExecutor retrySelector =
			new ThreadPoolExecutor(
				1,
				1,
				0L,
				TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>(),
				r -> new Thread(r, "retrySelector-Thread"));
		retrySelector.execute(new TaskProducer(queue));
	}

	@Override
	public void stop() {
	}

	public class TaskConsumer implements Runnable {

		private static final long MAX_SLEEP_TIME_MILLISECONDS = 10 * 1000L;
		private static final long SLEEP_BASE_TIME_MILLISECONDS = 1000L;
		private BlockingQueue<RetryContext> queue;
		private long sleepTimes = 0L;

		private TaskConsumer(BlockingQueue<RetryContext> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				doExecute();
				long totalTime = sleepTimes * SLEEP_BASE_TIME_MILLISECONDS;
				try {
					Thread.sleep(
						totalTime > MAX_SLEEP_TIME_MILLISECONDS ? MAX_SLEEP_TIME_MILLISECONDS
							: totalTime);
				} catch (InterruptedException e) {
					log.error("taskConsumer interruptedException error", e);
				}
			}
		}

		private void doExecute() {
			try {
				RetryContext context = queue.take();
				HandleResultEnum result = retryExecutor.doExecute(context);
				if (HandleResultEnum.SUCCESS == result) {
					sleepTimes = 0L;
				} else if (HandleResultEnum.FAILURE == result) {
					sleepTimes = 0L;
					queue.add(context);
				} else if (HandleResultEnum.STOP == result) {
					sleepTimes = 0L;
				} else if (HandleResultEnum.ERROR == result) {
					sleepTimes = 0L;
					// do nothing
				} else {
					sleepTimes++;
					queue.add(context);
				}
			} catch (InterruptedException e) {
				log.error("Retry execute failed when getting retry task", e);
			} catch (Throwable e) {
				log.error("Retry invoke failed", e);
			}
		}
	}

	public class TaskProducer implements Runnable {

		private static final long MAX_SLEEP_TIME_MILLISECONDS = 10 * 1000L;
		private static final long SLEEP_BASE_TIME_MILLISECONDS = 1000L;

		private BlockingQueue<RetryContext> queue;

		private long sleepTimes = 0L;

		private volatile Long lastId = 0L;

		public TaskProducer(BlockingQueue<RetryContext> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				doSelect();
				long totalTime =
					sleepTimes * SLEEP_BASE_TIME_MILLISECONDS + SLEEP_BASE_TIME_MILLISECONDS;
				try {
					Thread.sleep(
						totalTime > MAX_SLEEP_TIME_MILLISECONDS ? MAX_SLEEP_TIME_MILLISECONDS
							: totalTime);
				} catch (InterruptedException e) {
					log.error("taskConsumer interruptedException error", e);
				}
			}
		}

		private void doSelect() {
			if (queue.size() >= MAX_QUEUE_SIZE) {
				sleepTimes++;
				return;
			}

			List<RetryTask> tasks =
				retryConfiguration.getRetryTaskAccess().listAvailableTasks(namespace, lastId);
			if (CollectionUtils.isEmpty(tasks)) {
				sleepTimes++;
				return;
			}
			if (queue.size() >= MAX_QUEUE_SIZE) {
				sleepTimes++;
				return;
			}
			if (queue.size() + tasks.size() >= MAX_QUEUE_SIZE) {
				sleepTimes++;
			} else {
				sleepTimes = 0L;
			}
			for (RetryTask task : tasks) {
				try {
					lastId = task.getId();
					RetryContext retryContext =
						new RetryContextBuilder(retryConfiguration, task)
							.buildArgs()
							.buildExecutor()
							.buildMethod()
							.buildRetryArgSerializer()
							.buildStopStrategy()
							.buildWaitStrategy()
							.buildRetryTask()
							.buildMaxRetryTimes()
							.buildOnFailureMethod()
							.buildPriority(0L)
							.build();
					retryContext.start();

					queue.put(retryContext);
					log.warn("add retry task to queue. namespace:{}, task:{}", namespace,
						task.getId());
				} catch (Throwable e) {
					log.error("add retry task to queue. namespace:{}, task:{}", namespace,
						task.getId(), e);
					// 出现异常task将放不进queue中，就不会再重试了
					// 是否需要更新task的状态，并且加上失败的原因？
				}
			}
		}
	}
}
