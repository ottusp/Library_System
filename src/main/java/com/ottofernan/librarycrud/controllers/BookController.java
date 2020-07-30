package com.ottofernan.librarycrud.controllers;

import com.ottofernan.librarycrud.models.Book;
import com.ottofernan.librarycrud.services.HibernateSearchService;
import com.ottofernan.librarycrud.services.RestBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping({"/books", "/books/"})
public class BookController {

    @Autowired
    private final RestBookService restBookService;

    @Autowired
    private final HibernateSearchService searchService;

    public BookController(RestBookService restBookService, HibernateSearchService searchService) {
        this.restBookService = restBookService;
        this.searchService = searchService;
    }

    @GetMapping({"", "/", "/index", "/index.html"})
    public String getBooks(Model model){
        model.addAttribute("books", restBookService.findAllBooks());
        model.addAttribute("newBook", new Book());

        restBookService.findAllBooks().forEach(book -> {
            System.out.println(book.getAuthors());
        });

        return "books/listBooks.html";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "search", required = false) String query, Model model){
        List<Book> searchResults = null;
        try{
            searchResults = searchService.fuzzySearch(query);
        } catch (Exception ex){
            System.out.println("Error... " + searchResults);
        }

        model.addAttribute("search", searchResults);
        return "books/searchResults";
    }

    @PostMapping(path = "/post")
    public String postBook(@ModelAttribute("newBook") Book book){
        System.out.println(book);

        restBookService.create(book);
        return "books/index.html";
    }

}
