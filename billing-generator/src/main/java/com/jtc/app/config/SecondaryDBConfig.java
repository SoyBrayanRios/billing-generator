package com.jtc.app.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableJpaRepositories(entityManagerFactoryRef = "secondaryEntityManagerFactory", 
	transactionManagerRef = "secondaryTransactionManager", basePackages = {"com.jtc.app.secondary.dao"})
public class SecondaryDBConfig {
	
	@Autowired
	private Environment env;
	
	@Bean(name = "secondaryDataSource")
	public DataSource primaryDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(env.getProperty("secondary.datasource.url"));
		dataSource.setUsername(env.getProperty("secondary.datasource.username"));
		dataSource.setPassword(env.getProperty("secondary.datasource.password"));
		dataSource.setDriverClassName(env.getProperty("secondary.datasource.main.driver-class-name"));
		return dataSource;
	}
	
	@Bean(name = "secondaryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(primaryDataSource());
		em.setPackagesToScan("com.jtc.app.secondary.entity");
		
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		
		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("secondary.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.show-sql", env.getProperty("secondary.jpa.show-sql"));
		properties.put("hibernate.dialect", env.getProperty("secondary.jpa.database-platform"));
		properties.put("hibernate.dialect", env.getProperty("secondary.jpa.database-platform"));
		
		em.setJpaPropertyMap(properties);
		return em;
	}
	
	@Bean(name = "secondaryTransactionManager")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		
		return transactionManager;
	}
}
