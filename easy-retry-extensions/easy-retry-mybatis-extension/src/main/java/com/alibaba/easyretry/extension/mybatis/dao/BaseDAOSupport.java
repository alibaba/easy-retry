package com.alibaba.easyretry.extension.mybatis.dao;

import lombok.Setter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author wuhao
 */
public abstract class BaseDAOSupport {

	@Setter
	private SqlSessionFactory sqlSessionFactory;


	protected <T> T excute(Excute<T> excute) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			return excute.excute(session);
		}
	}

	public interface Excute<T> {

		T excute(SqlSession sqlSession);
	}

}
