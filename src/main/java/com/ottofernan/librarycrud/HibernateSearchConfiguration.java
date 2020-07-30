package com.ottofernan.librarycrud;

import com.ottofernan.librarycrud.services.HibernateSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@EnableAutoConfiguration
@Configuration
public class HibernateSearchConfiguration {

    @Autowired
    private EntityManager entityManager;

    @Bean
    HibernateSearchService hibernateSearchService(){
        HibernateSearchService hibernateSearchService = new HibernateSearchService(entityManager);
        hibernateSearchService.initializeHibernateSearch();
        return hibernateSearchService;
    }
}
