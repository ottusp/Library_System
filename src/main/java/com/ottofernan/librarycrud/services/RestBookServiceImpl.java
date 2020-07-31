package com.ottofernan.librarycrud.services;

import com.ottofernan.librarycrud.models.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class RestBookServiceImpl implements RestBookService {

    private final RestTemplate restTemplate;

    @Value("${resource.get_books}")
    private String getAllResource;

    @Value("${resource.get_books_by_title}")
    private String findByTitleResource;

    @Value("${resource.get_books}/{id}")
    private String idResource;

    public RestBookServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Book> findAllBooks(){
        return Arrays.asList(restTemplate.getForObject(getAllResource, Book[].class));
    }

    public List<Book> findByTitle(String title){
        String query = findByTitleResource + "?title=" + title;
        return Arrays.asList(restTemplate.getForObject(query, Book[].class));
    }

    public Book create(Book book){
        return restTemplate.postForObject(getAllResource, book, Book.class);
    }
}
