package com.pg.plantapps;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application{

	public static void main(String[] args) {
		SSLUtil.disableSslVerification();
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public BasicDataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		dataSource.setUrl("jdbc:sqlserver://PUGPAS:1433;databaseName=SOADB");
		 
		dataSource.setUsername("sa");
		dataSource.setPassword("fred");
		return dataSource;
	}
}
