package com.ottofernan.librarycrud.controllers

import com.ottofernan.librarycrud.domain.dtos.BookDTO
import com.ottofernan.librarycrud.domain.dtos.toModel
import com.ottofernan.librarycrud.domain.models.Book
import com.ottofernan.librarycrud.services.book.BookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookRestController(private val bookService: BookService) {

    @GetMapping("/get")
    fun getAll(): ResponseEntity<Set<BookDTO>> {
        val books: Set<BookDTO> = bookService.findAll()
        return ResponseEntity.ok().body(books)
    }

    @GetMapping("/get/{id}")
    fun getById(@PathVariable("id") id: Long): ResponseEntity<BookDTO?> {
        val book: BookDTO? = bookService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(book)
    }

    @GetMapping("/getByTitle")
    fun getByTitle(@RequestParam title: String): ResponseEntity<Set<BookDTO>> {
        val books = bookService.findAllByTitle(title)
        return ResponseEntity.status(HttpStatus.OK).body(books)
    }

    @PostMapping
    fun postBook(@RequestBody book: BookDTO): ResponseEntity<Any?> {
        val checkedBook: BookDTO?

        if(Book.isValid(toModel(book))) {
            checkedBook = bookService.save(book)
            return ResponseEntity.status(HttpStatus.CREATED).body(checkedBook)
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("This is not a valid book")

    }

    @PutMapping(consumes = ["application/json"])
    fun updateBook(@RequestBody book: BookDTO): ResponseEntity<Any> {
        val checkedBook = bookService.findById(book.id)
        if(checkedBook != null) {
            bookService.save(book)
            return ResponseEntity.status(HttpStatus.ACCEPTED).build()
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build()
    }


}