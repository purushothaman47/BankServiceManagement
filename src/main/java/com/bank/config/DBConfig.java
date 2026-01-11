package com.bank.config;

import com.bank.exception.DataException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

public final class DBConfig {

    private static final Logger LOG =
            LoggerFactory.getLogger(DBConfig.class);

    private static final HikariDataSource dataSource;

    private DBConfig() {
    }

    static {
        try {
            LOG.info("Initializing Database Configuration");

            Properties props = new Properties();
            try (InputStream in =
                         Thread.currentThread()
                                 .getContextClassLoader()
                                 .getResourceAsStream("db.properties")) {

                if (in == null) {
                    throw new DataException("db.properties not found in classpath");
                }
                props.load(in);
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.username"));
            config.setPassword(props.getProperty("db.password"));
            config.setDriverClassName(props.getProperty("db.driver"));

            config.setMaximumPoolSize(
                    Integer.parseInt(props.getProperty("hikari.maximumPoolSize", "10")));
            config.setMinimumIdle(
                    Integer.parseInt(props.getProperty("hikari.minimumIdle", "2")));
            config.setConnectionTimeout(
                    Long.parseLong(props.getProperty("hikari.connectionTimeout", "30000")));
            config.setIdleTimeout(
                    Long.parseLong(props.getProperty("hikari.idleTimeout", "600000")));
            config.setMaxLifetime(
                    Long.parseLong(props.getProperty("hikari.maxLifetime", "1800000")));

            dataSource = new HikariDataSource(config);

            LOG.info("HikariCP DataSource initialized successfully");

        } catch (Exception e) {
            throw new DataException("Failed to initialize DB", e);
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
