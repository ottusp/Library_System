package com.ottofernan.librarycrud.controllers;

import com.ottofernan.librarycrud.models.Book;
import com.ottofernan.librarycrud.services.restbook.RestBookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({"/books", "/books/"})
public class BookController {

    private final RestBookService restBookService;

    public BookController(RestBookService restBookService) {
        this.restBookService = restBookService;
    }

    @GetMapping({"/list", "/listBooks"})
    public String getBooks(Model model){
        model.addAttribute("books", restBookService.findAllBooks());
        model.addAttribute("newBook", new Book());

        return "books/listBooks";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute("title") String title, Model model){

        if(title == null) {
            model.addAttribute("title", "");
        } else{
            model.addAttribute("results", restBookService.findByTitle(title));
        }
        model.addAttribute("rented_book_id");
        return "books/searchResults";
    }

    @GetMapping("/returnBook")
    public String returnBook(){
        return "notImplemented";
    }

    @GetMapping("/donate")
    public String donate(){
        return "notImplemented";
    }

    @GetMapping("/rent")
    public String rentBook(@ModelAttribute("rented_book_id") Long id){
        System.out.println("On rent, book is " + id);
        return "notImplemented";
    }

    @PostMapping("/post")
    public String postBook(@ModelAttribute("newBook") Book book){
        System.out.println(book);

        restBookService.create(book);
        return "index";
    }

}
