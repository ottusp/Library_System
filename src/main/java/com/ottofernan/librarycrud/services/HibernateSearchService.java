package com.ottofernan.librarycrud.services;

import com.ottofernan.librarycrud.models.Book;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

//@Service
public class HibernateSearchService{

    @Autowired
    private final EntityManager entityManager;

    @Autowired
    public HibernateSearchService(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    public void initializeHibernateSearch(){
        try{
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Transactional
    public List<Book> fuzzySearch(String searchTerm){
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(Book.class).get();
        Query luceneQuery = queryBuilder.keyword().fuzzy().withEditDistanceUpTo(1).withPrefixLength(1)
                .onFields("title").matching(searchTerm).createQuery();

        javax.persistence.Query jpaQuery = fullTextEntityManager
                .createFullTextQuery(luceneQuery, Book.class);

        List<Book> bookList = null;
        try{
            bookList = jpaQuery.getResultList();
        } catch (NoResultException nre){
            System.err.println("Error on searching query \"" + searchTerm + "\": " + nre);
        }

        return bookList;

    }
}
