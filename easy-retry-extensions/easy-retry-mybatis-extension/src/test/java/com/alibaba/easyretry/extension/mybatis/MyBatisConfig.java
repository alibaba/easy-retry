package com.alibaba.easyretry.extension.mybatis;

import java.io.IOException;
import lombok.Getter;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class MyBatisConfig {

	@Getter
	private static final SqlSessionFactory factory;

	static {
		String resource = "dal/easyretry/easy-mybatis-config.xml";
		Configuration parse;
		try {
			XMLConfigBuilder parser = new XMLConfigBuilder(Resources.getResourceAsStream(resource),
				null, null);
			parse = parser.parse();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		final Environment development = new Environment("development",
			new JdbcTransactionFactory(), DbConfig.getDataSource());
		parse.setEnvironment(development);
		parse.setLogImpl(StdOutImpl.class);
		factory = new SqlSessionFactoryBuilder().build(parse);
	}

	@Test
	void testNotNull() {
		Assertions.assertNotNull(MyBatisConfig.getFactory());
	}
}
