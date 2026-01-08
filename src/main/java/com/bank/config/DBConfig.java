package com.bank.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

public class DBConfig {

    private static final Logger log =
            LoggerFactory.getLogger(DBConfig.class);

    private static final HikariDataSource dataSource;

    static {
        try {
            log.info("Initializing Database Configuration");
            Properties props = new Properties();

            InputStream is = DBConfig.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties");

//            if (is == null) {
//
//                throw new RuntimeException("");
//            }

            props.load(is);
            log.info("Database properties loaded successfully");
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));

            config.setUsername(props.getProperty("db.username"));

            config.setPassword(props.getProperty("db.password"));

            config.setDriverClassName(props.getProperty("db.driver"));

            dataSource = new HikariDataSource(config);
            log.info("HikariCP DataSource initialized successfully");


        } catch (Exception e) {
            log.error("Database Initialization Failed");
            throw new RuntimeException("DB Initialization Failed", e);
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
