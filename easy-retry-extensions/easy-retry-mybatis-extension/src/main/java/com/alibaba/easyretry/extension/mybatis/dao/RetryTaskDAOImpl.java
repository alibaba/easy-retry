package com.alibaba.easyretry.extension.mybatis.dao;

import com.alibaba.easyretry.extension.mybatis.po.RetryTaskPO;
import com.alibaba.easyretry.extension.mybatis.query.RetryTaskQuery;
import java.util.List;

/**
 * @author Created by wuhao on 2020/11/8.
 */
public class RetryTaskDAOImpl extends BaseDAOSupport implements RetryTaskDAO {

	@Override
	public boolean saveRetryTask(RetryTaskPO retryTaskPO) {
		return excute(sqlSession -> sqlSession.insert(
			"com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO.saveRetryTask",
			retryTaskPO)
			> 0);
	}

	@Override
	public List<RetryTaskPO> listRetryTask(RetryTaskQuery retryTaskQuery) {
		return excute(sqlSession -> sqlSession.selectList(
			"com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO.listRetryTask",
			retryTaskQuery));
	}

	@Override
	public boolean updateRetryTask(RetryTaskPO retryTaskPO) {
		return excute(sqlSession -> sqlSession.update(
			"com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO.updateRetryTask",
			retryTaskPO) > 0);
	}

	@Override
	public boolean deleteRetryTask(RetryTaskPO retryTaskPO) {
		return excute(sqlSession -> sqlSession.delete(
			"com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO.deleteRetryTask",
			retryTaskPO)
			> 0);
	}
}
