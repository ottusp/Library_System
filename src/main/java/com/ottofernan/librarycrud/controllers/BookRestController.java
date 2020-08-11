package com.ottofernan.librarycrud.controllers;

import com.ottofernan.librarycrud.domain.dtos.BookDTO;
import com.ottofernan.librarycrud.domain.models.Book;
import com.ottofernan.librarycrud.services.book.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.ottofernan.librarycrud.domain.dtos.BookDTOKt.toModel;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/get")
    public Set<BookDTO> getAll(){

        return bookService.findAll();
    }

    @GetMapping("/get/{id}")
    public BookDTO getById(@PathVariable("id") Long id){
        return bookService.findById(id);
    }

    @GetMapping("/getByTitle")
    public Set<BookDTO> getByTitle(@ModelAttribute("title") String title){
        if(title == null) return null;

        return bookService.findAllByTitle(title);
    }

    @PostMapping("/post")
    public BookDTO postBook(@RequestBody BookDTO book){
        if(Book.isValid(toModel(book))){
            bookService.save(book);
            return book;
        }
        return null; // TODO: retornar uma ResponseEntity
    }

    @PutMapping(value = "/post", consumes = "application/json")
    public void updateBook(@RequestBody BookDTO book){
        if(book != null){
            BookDTO checkedBook = bookService.findById(book.getId());
            if(checkedBook != null) {
                bookService.save(book);
            }
        }
    }
}
