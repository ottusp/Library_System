package com.ottofernan.librarycrud.controllers

import com.ottofernan.librarycrud.models.Book
import com.ottofernan.librarycrud.services.book.BookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookRestController(private val bookService: BookService) {

    @GetMapping("/get")
    fun getAll(): ResponseEntity<Set<Book>> {
        val books: Set<Book> = bookService.findAll()
        return ResponseEntity.ok().body(books)
    }

    @GetMapping("/get/{id}")
    fun getById(@PathVariable("id") id: Long): ResponseEntity<Book?> {
        val book: Book? = bookService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(book)
    }

    @GetMapping("/getByTitle")
    fun getByTitle(@RequestParam title: String): ResponseEntity<Set<Book>> {
        val books = bookService.findAllByTitle(title)
        return ResponseEntity.status(HttpStatus.OK).body(books)
    }

    @PostMapping
    fun postBook(@RequestBody book: Book): ResponseEntity<Any?> {
        val checkedBook: Book?

        if(Book.isValid(book)) {
            checkedBook = bookService.save(book)
            return ResponseEntity.status(HttpStatus.CREATED).body(checkedBook)
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("This is not a valid book")

    }

    @PutMapping(consumes = ["application/json"])
    fun updateBook(@RequestBody book: Book): ResponseEntity<Any> {
        val checkedBook = bookService.findById(book.id)
        if(checkedBook != null) {
            bookService.save(book)
            return ResponseEntity.status(HttpStatus.ACCEPTED).build()
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build()
    }


}