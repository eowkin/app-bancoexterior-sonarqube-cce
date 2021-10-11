package com.bancoexterior.app.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bancoexterior.app.seguridad.MiCipher;




@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "adminEntityManagerFactory", transactionManagerRef = "adminTransactionManager", 
basePackages = { "com.bancoexterior.app.cce.repository" })
public class PostgreSQLCCEConfig {

	
	private BasicDataSource db = new BasicDataSource();
	
	@Value("${${app.ambiente}"+".seed.monitorfinanciero}")
    private String sconfigDesKey;
	
	@Value("${postgre.jpa.database-platform}")
    private String postgreJpaDatabasePlatform;
	
	@Value("${${app.ambiente}"+".dbCce.user}")
    private String usuario;
	
	@Value("${postgre.jpa.hibernate.ddl-auto}")
    private String postgreJpaHibernateDdlAuto;
	
    @Value("${${app.ambiente}"+".dbCce.password}")
    private String clave;
    
    @Value("${postgre.jpa.show-sql}")
    private String postgreJpaShowSql;
    
    @Value("${${app.ambiente}"+".dbCce.url}")
    private String url;
    
    @Value("${dbCce.validationQueryTimeout}")
    private String validationQueryTimeout;
    
    @Value("${dbCce.driver}")
    private String driver;
    
    @Value("${dbCce.validationQuery}")
    private String validationQuery;
    
    @Value("${dbCce.initialSize}")
    private String initialSize;
    
    @Value("${dbCce.removeAbandonedOnMaintenance}")
    private String removeAbandonedOnMaintenance;
    
    @Value("${dbCce.testOnBorrow}")
    private String testOnBorrow;
    
    @Value("${dbCce.defaultAutoCommit}")
    private String defaultAutoCommit;
    
    @Value("${dbCce.testOnReturn}")
    private String testOnReturn;
    
    @Value("${dbCce.minEvictableIdleTimeMillis}")
    private String minEvictableIdleTimeMillis;
    
    @Value("${dbCce.testWhileIdle}")
    private String testWhileIdle;
    
    @Value("${dbCce.logAbandoned}")
    private String logAbandoned;
    
    @Value("${dbCce.timeBetweenEvictionRunsMillis}")
    private String timeBetweenEvictionRunsMillis;
    
    @Value("${dbCce.removeAbandonedTimeout}")
    private String removeAbandonedTimeout;
    
    @Value("${dbCce.minIdle}")
    private String minIdle;
    
    @Value("${dbCce.removeAbandonedOnBorrow}")
    private String removeAbandonedOnBorrow;
    
    @Value("${dbCce.maxTotal}")
    private String maxTotal;
    
    @Value("${dbCce.maxWaitMillis}")
    private String maxWaitMillis;
    
    @Value("${dbCce.maxIdle}")
    private String maxIdle;
    
    
    
    
    
    
    
    
    
	
    
    
    
    
    
    @Bean(name = "adminDataSource")
	public DataSource adminDatasource() {
		
		 db.setUsername(MiCipher.decrypt(usuario.trim(), sconfigDesKey.trim()));
		 db.setValidationQueryTimeout(Integer.parseInt(validationQueryTimeout));
		 db.setPassword(MiCipher.decrypt(clave.trim(), sconfigDesKey.trim()));
		 db.setValidationQuery(validationQuery);
		 db.setUrl(url);
		 db.setRemoveAbandonedOnMaintenance(Boolean.parseBoolean(removeAbandonedOnMaintenance));
		 db.setDriverClassName(driver);
		 db.setDefaultAutoCommit(Boolean.parseBoolean(defaultAutoCommit));
         db.setInitialSize(Integer.parseInt(initialSize));
         db.setMinEvictableIdleTimeMillis(Long.parseLong(minEvictableIdleTimeMillis));
         db.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
         db.setLogAbandoned(Boolean.parseBoolean(logAbandoned));
         db.setTestOnReturn(Boolean.parseBoolean(testOnReturn));
         db.setRemoveAbandonedTimeout(Integer.parseInt(removeAbandonedTimeout));
         db.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));
         db.setRemoveAbandonedOnBorrow(Boolean.parseBoolean(removeAbandonedOnBorrow));
         db.setTimeBetweenEvictionRunsMillis(Long.parseLong(timeBetweenEvictionRunsMillis));
         db.setMaxWaitMillis(Long.parseLong(maxWaitMillis));
         db.setMinIdle(Integer.parseInt(minIdle));
         db.setMaxIdle(Integer.parseInt(maxIdle));
         db.setMaxTotal(Integer.parseInt(maxTotal));
        
		return db;
	}
	

	@Bean(name = "adminEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(adminDatasource());
		em.setPackagesToScan("com.bancoexterior.app.cce.model");
		
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		
		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", postgreJpaHibernateDdlAuto);
		properties.put("hibernate.show-sql", postgreJpaShowSql);
		properties.put("hibernate.dialect", postgreJpaDatabasePlatform);
		
		em.setJpaPropertyMap(properties);
		
		return em;
		
	}
	
	
	@Bean(name = "adminTransactionManager")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		
		return transactionManager;
	}
}
