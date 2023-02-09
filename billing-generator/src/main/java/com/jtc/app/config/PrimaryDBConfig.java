package com.jtc.app.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "primaryEntityManagerFactory", 
	transactionManagerRef = "primaryTransactionManager", basePackages = {"com.jtc.app.primary.dao"})
public class PrimaryDBConfig {
	
	@Autowired
	private Environment env;
	
	@Bean(name = "primaryDataSource")
	public DataSource primaryDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(env.getProperty("primary.datasource.url"));
		dataSource.setUsername(env.getProperty("primary.datasource.username"));
		dataSource.setPassword(env.getProperty("primary.datasource.password"));
		dataSource.setDriverClassName(env.getProperty("primary.datasource.main.driver-class-name"));
		return dataSource;
	}
	
	@Bean(name = "primaryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(primaryDataSource());
		em.setPackagesToScan("com.jtc.app.primary.entity");
		
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		
		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("primary.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.show-sql", env.getProperty("primary.jpa.show-sql"));
		properties.put("hibernate.dialect", env.getProperty("primary.jpa.database-platform"));
		properties.put("hibernate.dialect", env.getProperty("primary.jpa.database-platform"));
		
		em.setJpaPropertyMap(properties);
		return em;
	}
	
	@Bean(name = "primaryTransactionManager")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		
		return transactionManager;
	}
}
