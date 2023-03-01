package com.example.secondhandmarketwebapp;

import java.util.Properties;
import javax.sql.DataSource;

import com.example.secondhandmarketwebapp.storage.StorageProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//Todo edit
@Configuration
@EnableWebMvc
@EnableConfigurationProperties(StorageProperties.class)
public class ApplicationConfig {

    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean sessionFactory() {
        String PACKAGE_NAME = "com.example.secondhandmarketwebapp.entity";
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(PACKAGE_NAME);
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        String RDS_ENDPOINT = "laiflagcomp-instance.cfs56ldxvzwh.us-west-1.rds.amazonaws.com";
        String USERNAME = "admin";
        String PASSWORD = "12345678";
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + RDS_ENDPOINT + ":3306/secondhandMarket?createDatabaseIfNotExist=true&serverTimezone=UTC");
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        return dataSource;
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        return hibernateProperties;
    }
}