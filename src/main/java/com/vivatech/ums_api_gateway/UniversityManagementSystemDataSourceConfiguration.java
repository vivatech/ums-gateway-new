/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vivatech.ums_api_gateway;

/**
 * @author KALAM
 */

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.vivatech.ums_api_gateway.repository"}, entityManagerFactoryRef = "umsportalEntityManagerFactory", transactionManagerRef = "umsportalTransactionManager")
//@PropertySource("classpath:database.properties")
@PropertySource("file:////home/core/ums/gateway/database.properties")
public class UniversityManagementSystemDataSourceConfiguration {

  @Bean
  @Primary
  @ConfigurationProperties("app.datasource.umsportal")
  public DataSourceProperties umsportalDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @Primary
  @ConfigurationProperties("app.datasource.umsportal.configuration.hikari")
  public DataSource umsportalGatewayDataSource() {
    return umsportalDataSourceProperties().initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
  }

  @Primary
  @Bean(name = "umsportalEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean umsportalEntityManagerFactory(EntityManagerFactoryBuilder builder) {
    return builder.dataSource(umsportalGatewayDataSource()).packages(new String[]{"com.vivatech.ums_api_gateway.model"}).build();
  }

  @Primary
  @Bean
  public PlatformTransactionManager umsportalTransactionManager(final @Qualifier("umsportalEntityManagerFactory") LocalContainerEntityManagerFactoryBean umsportalEntityManagerFactory) {
    return new JpaTransactionManager(umsportalEntityManagerFactory.getObject());
  }

}
