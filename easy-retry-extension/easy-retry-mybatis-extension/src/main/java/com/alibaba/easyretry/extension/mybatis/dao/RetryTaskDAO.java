package com.alibaba.easyretry.extension.mybatis.dao;

import java.util.List;

import com.alibaba.easyretry.extension.mybatis.po.RetryTaskPO;
import com.alibaba.easyretry.extension.mybatis.query.RetryTaskQuery;

/**
 * @author Created by wuhao on 2020/11/8.
 */
public interface RetryTaskDAO {

    boolean saveRetryTask(RetryTaskPO retryTaskPO);

    List<RetryTaskPO> listRetryTask(RetryTaskQuery retryTaskQuery);

    boolean updateRetryTask(RetryTaskPO retryTaskPO);

    boolean deleteRetryTask(RetryTaskPO retryTaskPO);

}
