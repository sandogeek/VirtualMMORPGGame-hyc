package com.mmorpg.mbdl.framework.storage.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.mmorpg.mbdl.framework.storage.core.StorageMySql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(basePackages = "com.mmorpg.mbdl.bussiness.**.dao",
        repositoryBaseClass = StorageMySql.class)
@ImportResource(locations = {"classpath*:applicationContext.xml"})
public class SpringDataJpaConfig {
    @Bean
    HibernateJpaVendorAdapter jpaVendorAdapter(){
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setShowSql(false);
        hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL55Dialect");
        return hibernateJpaVendorAdapter;
    }
    @Bean
    @Autowired
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DruidDataSource druidDataSource, JpaVendorAdapter jpaVendorAdapter){
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(druidDataSource);
        entityManagerFactoryBean.setPackagesToScan("com.mmorpg.**.entity");
        entityManagerFactoryBean.setJpaDialect(new HibernateJpaDialect());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        // Map<String,?> jpaPropertyMap = new HashMap<>(10);
        // TODO jpaProperty优化
        // jpaProperties.put()
        // entityManagerFactoryBean.setJpaPropertyMap();
        return entityManagerFactoryBean;
    }
    @Bean
    @Autowired
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }
}
