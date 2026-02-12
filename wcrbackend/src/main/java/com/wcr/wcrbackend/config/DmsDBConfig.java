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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.wcr.wcrbackend.dms.repository",   
        entityManagerFactoryRef = "dmsEntityManagerFactory",
        transactionManagerRef = "dmsTransactionManager"
)
public class DmsDBConfig {

    @Value("${dms.datasource.jdbcUrl}")
    private String jdbcUrl;

    @Value("${dms.datasource.username}")
    private String username;

    @Value("${dms.datasource.password}")
    private String password;

    @Value("${dms.datasource.driver-class-name}")
    private String driverClassName;

    @Bean(name = "dmsDataSource")
    public DataSource dmsDataSource() {
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

    @Bean(name = "dmsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean dmsEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "none");
        props.put("hibernate.show_sql", true);
      
        return builder
                .dataSource(dmsDataSource())
                .packages("com.wcr.wcrbackend.dms.entity")   
                .persistenceUnit("dmsPU")
                .properties(props)
                .build();
    }

    @Bean(name = "dmsTransactionManager")
    public PlatformTransactionManager dmsTransactionManager(
            @Qualifier("dmsEntityManagerFactory") EntityManagerFactory emf) {

        return new JpaTransactionManager(emf);
    }
}
