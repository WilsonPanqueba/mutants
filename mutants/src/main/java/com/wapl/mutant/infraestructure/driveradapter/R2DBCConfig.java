package com.wapl.mutant.infraestructure.driveradapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;


@Configuration
public class R2DBCConfig {
  @Value("${postgresql.drive}")
  private  String driver;
  @Value("${postgresql.host}")
  private  String host;
  @Value("${postgresql.port}")
  private  Integer port;
  @Value("${postgresql.user}")
  private  String user;
  @Value("${postgresql.password}")
  private  String password;
  @Value("${postgresql.database}")
  private  String database;

    @Bean
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(ConnectionFactoryOptions.DRIVER, driver)
                        .option(ConnectionFactoryOptions.HOST, host)
                        .option(ConnectionFactoryOptions.PORT, port)
                        .option(ConnectionFactoryOptions.USER, user)
                        .option(ConnectionFactoryOptions.PASSWORD, password)
                        .option(ConnectionFactoryOptions.DATABASE, database)
                        .build());
    }
}