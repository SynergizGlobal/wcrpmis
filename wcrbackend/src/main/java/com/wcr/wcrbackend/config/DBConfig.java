package com.wcr.wcrbackend.config;
import java.util.HashMap;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.wcr.wcrbackend",
    entityManagerFactoryRef = "wcrpmisEntityManagerFactory",
    transactionManagerRef = "wcrpmisTransactionManager"
)
public class DBConfig {

    @Value("${wcrpmis.datasource.jdbcUrl}")
    private String jdbcUrl;

    @Value("${wcrpmis.datasource.username}")
    private String username;

    @Value("${wcrpmis.datasource.password}")
    private String password;

    @Value("${wcrpmis.datasource.driver-class-name}")
    private String driverClassName;

    @Primary
    @Bean(name= "wcrpmisDataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(
            @Qualifier("wcrpmisDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Primary
    @Bean(name = "wcrpmisEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("wcrpmisDataSource") DataSource dataSource) {
 
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none"); 
 
        return builder
                .dataSource(dataSource)
                .packages("com.wcr.wcrbackend")
                .persistenceUnit("wcrpmisPU")
                .properties(properties) // ✅ Apply properties here
                .build();
    }
 
    @Primary
    @Bean(name = "wcrpmisTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("wcrpmisEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    
    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(),
                new HashMap<>(),
                null
        );
    }

}