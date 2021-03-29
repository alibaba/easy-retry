package com.alibaba.easyretry.extension.mybatis.dao;

import com.alibaba.easyretry.extension.mybatis.po.RetryTaskPO;
import com.alibaba.easyretry.extension.mybatis.query.RetryTaskQuery;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author Created by wuhao on 2020/11/8.
 */
public class RetryTaskDAOImplV2 implements RetryTaskDAO {

	private final SqlSessionFactory sqlSessionFactory;

	public RetryTaskDAOImplV2(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public boolean saveRetryTask(RetryTaskPO retryTaskPO) {
		try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
			return sqlSession.insert(
				"com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO.saveRetryTask",
				retryTaskPO)
				> 0;
		}
	}

	@Override
	public List<RetryTaskPO> listRetryTask(RetryTaskQuery retryTaskQuery) {
		try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
			return sqlSession.selectList(
				"com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO.listRetryTask",
				retryTaskQuery);
		}
	}

	@Override
	public boolean updateRetryTask(RetryTaskPO retryTaskPO) {
		try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
			return sqlSession.update(
				"com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO.updateRetryTask",
				retryTaskPO)
				> 0;
		}
	}

	@Override
	public boolean deleteRetryTask(RetryTaskPO retryTaskPO) {
		try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
			return sqlSession.delete(
				"com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO.deleteRetryTask",
				retryTaskPO)
				> 0;
		}
	}
}
