package com.ottofernan.librarycrud.controllers;

import com.ottofernan.librarycrud.models.Book;
import com.ottofernan.librarycrud.services.book.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/get")
    public Set<Book> getAll(){

        return bookService.findAll();
    }

    @GetMapping("/get/{id}")
    public Book getById(@PathVariable("id") Long id){
        return bookService.findById(id);
    }

    @GetMapping("/getByTitle")
    public Set<Book> getByTitle(@ModelAttribute("title") String title){

        if(title != null){
            return bookService.findAllByTitle(title);
        } else {
            return null;
        }
    }

    @PostMapping("/post")
    public Book postBook(@RequestBody Book book){
        if(Book.isValid(book)){
            bookService.save(book);
        }
        return book;
    }

    @PutMapping(value = "/post", consumes = "application/json")
    public void updateBook(@RequestBody Book book){
        if(book != null){
            Book checkedBook = bookService.findById(book.getId());
            if(checkedBook != null) {
                bookService.save(book);
            }
        }
    }
}
