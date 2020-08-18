package com.ottofernan.librarycrud.services.restbook;

import com.ottofernan.librarycrud.domain.dtos.BookDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RestBookServiceImpl implements RestBookService {

    private final RestTemplate restTemplate;

    @Value("${resource.api_books}")
    private String apiBookResource;

    @Value("${resource.get_books_by_title}")
    private String findByTitleResource;

    @Value("${resource.api_books}/{id}")
    private String idResource;

    public RestBookServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<BookDTO> findAllBooks(){
        return Arrays.asList(restTemplate.getForObject(apiBookResource, BookDTO[].class));
    }

    public List<BookDTO> findByTitle(String title){
        try {
            String query = findByTitleResource + "?title=" + title;
            return Arrays.asList(restTemplate.getForObject(query, BookDTO[].class));
        } catch (HttpClientErrorException e){
            System.out.println("Não encontrado!");
            return new ArrayList<>();
        }
    }

    public BookDTO findById(Long id){
        String url = apiBookResource + "/" + id;
        return restTemplate.getForObject(url, BookDTO.class);
    }

    public BookDTO create(BookDTO book){
        return restTemplate.postForObject(apiBookResource, book, BookDTO.class);
    }

    public void update(BookDTO book){
        if(book == null) return;
        String url = apiBookResource;
        restTemplate.put(url, book);
    }

}
