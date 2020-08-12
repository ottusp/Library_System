package com.ottofernan.librarycrud.controllers

import com.ottofernan.librarycrud.models.Book
import com.ottofernan.librarycrud.services.book.BookService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookRestController(private val bookService: BookService) {

    @GetMapping("/get")
    fun getAll(): Set<Book> =
            bookService.findAll()

    @GetMapping("/get/{id}")
    fun getById(@PathVariable("id") id: Long) =
            bookService.findById(id)

    @GetMapping("/getByTitle")
    fun getByTitle(@ModelAttribute("title") title: String): Set<Book> =
            bookService.findAllByTitle(title)

    @PostMapping
    fun postBook(@RequestBody book: Book): Book {
        if(Book.isValid(book)){
            bookService.save(book)
        }
        return book
    }

    @PutMapping(consumes = ["application/json"])
    fun updateBook(@RequestBody book: Book) {
        val checkedBook = bookService.findById(book.id)
        if(checkedBook != null) {
            bookService.save(book)
        }
    }


}