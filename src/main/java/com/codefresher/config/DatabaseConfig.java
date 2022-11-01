package com.codefresher.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    @Value("${db.datasource.url}")
    private String url;
    @Value("${db.datasource.username}")
    private String username;
    @Value("${db.datasource.password}")
    private String password;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        String secretKey = "Tuyen24032001";
        config.setJdbcUrl(AESConfig.decrypt(url, secretKey));
        config.setUsername(AESConfig.decrypt(username, secretKey));
        config.setPassword(AESConfig.decrypt(password, secretKey));
        return new HikariDataSource(config);
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }
}
