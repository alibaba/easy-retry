package com.alibaba.easyretry.extension.mybatis.dao;

import java.util.List;

import com.alibaba.easyretry.extension.mybatis.po.RetryTaskPO;
import com.alibaba.easyretry.extension.mybatis.query.RetryTaskQuery;

/**
 * @author Created by wuhao on 2020/11/8.
 */
public class RetryTaskDAOImpl extends BaseDAOSupport implements RetryTaskDAO {

    @Override
    public boolean saveRetryTask(RetryTaskPO retryTaskPO) {
        return this.getSqlSession().insert("com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO.saveRetryTask", retryTaskPO) > 0;
    }

    @Override
    public List<RetryTaskPO> listRetryTask(RetryTaskQuery retryTaskQuery) {
        return this.getSqlSession().selectList("com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO.listRetryTask", retryTaskQuery);
    }

    @Override
    public boolean updateRetryTask(RetryTaskPO retryTaskPO) {
        return this.getSqlSession().update("com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO.updateRetryTask", retryTaskPO) > 0;
    }

    @Override
    public boolean deleteRetryTask(RetryTaskPO retryTaskPO) {
        return this.getSqlSession().delete("com.alibaba.easyretry.extension.mybatis.dao.RetryTaskDAO.deleteRetryTask", retryTaskPO) > 0;
    }

}
