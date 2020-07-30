package com.ottofernan.librarycrud.controllers;

import com.ottofernan.librarycrud.models.Book;
import com.ottofernan.librarycrud.repositories.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookRepository bookRepository;

    public BookRestController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/getAll")
    public Set<Book> getAll(){
        Set<Book> books = new HashSet<>();
        bookRepository.findAll().forEach(books::add);

        return books;
    }

    @GetMapping("/get/{id}")
    public Book getById(@PathVariable("id") Long id){
        if(id != null) {
            Book book = bookRepository.findById(id).orElse(null);
            return book;
        } else {
            return null;
        }
    }

    @PostMapping("/post")
    public Book postBook(@ModelAttribute("newBook") Book book){
        if(book != null){
            if(!book.getTitle().isEmpty() && !book.getIsbn().isEmpty()
                    && !book.getPublisher().isEmpty()) {
                bookRepository.save(book);
            }
        }

        return book;
    }
}
