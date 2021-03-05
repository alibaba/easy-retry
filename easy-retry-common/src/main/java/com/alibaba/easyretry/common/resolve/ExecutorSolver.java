package com.alibaba.easyretry.common.resolve;

/**
 * @author Created by wuhao on 2020/11/8.
 */
public interface ExecutorSolver {

    Object resolver(String executorName);

}
