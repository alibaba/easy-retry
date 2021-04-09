package com.alibaba.easyretry.extension.mybatis.dao;

import java.util.Objects;
import java.util.function.Function;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author wuhao
 */
public abstract class BaseDAOSupport {

	private final SqlSessionFactory sqlSessionFactory;

	public BaseDAOSupport(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	protected <T> T execute(Function<SqlSession, T> function) {
		Objects.requireNonNull(sqlSessionFactory, "require sqlSessionFactory non null");
		try (final SqlSession session = sqlSessionFactory.openSession()) {
			return function.apply(session);
		}
	}
}
