package com.alibaba.easyretry.extension.mybatis.dao;

import lombok.Setter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * @author wuhao
 */
public class BaseDAOSupport extends SqlSessionDaoSupport {

    @Setter
    private SqlSessionFactory sqlSessionFactory;

    public void init() {
        super.setSqlSessionFactory(sqlSessionFactory);
    }
}
