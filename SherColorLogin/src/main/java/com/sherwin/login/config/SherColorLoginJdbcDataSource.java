
package com.sherwin.login.config;

import oracle.jdbc.pool.OracleDataSource;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.sherwin.login",
        entityManagerFactoryRef = "shercolorLoginEntityManager",
        transactionManagerRef = "shercolorLoginTransactionManager")
@Profile("login-test")
public class SherColorLoginJdbcDataSource {

    @Autowired
    private Environment env;

    @Bean
    public PoolDataSource shercolorLoginDataSource() throws SQLException {
        PoolDataSource poolDataSource = PoolDataSourceFactory.getPoolDataSource();
        poolDataSource.setURL(env.getProperty("spring.shercolorlogin-datasource.url"));
        poolDataSource.setUser(env.getProperty("spring.shercolorlogin-datasource.username"));
        poolDataSource.setPassword(env.getProperty("spring.shercolorlogin-datasource.password"));
        poolDataSource.setConnectionFactoryClassName(OracleDataSource.class.getName());
        poolDataSource.setConnectionPoolName(env.getProperty("spring.shercolorlogin-datasource.oracleucp.connection-pool-name"));
        poolDataSource.setInactiveConnectionTimeout(120);
        poolDataSource.setMaxPoolSize(20);
        poolDataSource.setMinPoolSize(5);
        poolDataSource.setMaxStatements(10);
        poolDataSource.setValidateConnectionOnBorrow(true);
        return poolDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean shercolorLoginEntityManager() throws SQLException, NamingException {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(shercolorLoginDataSource());
        em.setPackagesToScan("com.sherwin.login");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setPersistenceUnitName("login");
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect",
                env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager shercolorLoginTransactionManager() throws SQLException, NamingException {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                shercolorLoginEntityManager().getObject());
        return transactionManager;
    }
}