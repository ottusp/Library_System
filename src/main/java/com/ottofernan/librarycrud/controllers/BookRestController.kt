package com.ottofernan.librarycrud.controllers

import com.ottofernan.librarycrud.domain.dtos.BookDTO
import com.ottofernan.librarycrud.domain.exceptions.EntityNotFoundException
import com.ottofernan.librarycrud.domain.exceptions.InvalidBookException
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
    fun getById(@PathVariable("id") id: Long): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(id)
            ResponseEntity.ok(book)
        } catch (enfe: EntityNotFoundException){
            ResponseEntity.badRequest().body(enfe.message)
        }
    }

    @GetMapping("/getByTitle")
    fun getByTitle(@RequestParam title: String): ResponseEntity<Any> {

        return try {
            val books = bookService.findAllByTitle(title)
            ResponseEntity.ok(books)
        } catch (enfe: EntityNotFoundException){
            ResponseEntity.badRequest().body(enfe.message)
        }
    }

    @PostMapping
    fun postBook(@RequestBody book: BookDTO): ResponseEntity<Any?> {

        return try {
            val checkedBook = bookService.save(book)
            ResponseEntity.status(HttpStatus.CREATED).body(checkedBook)
        } catch (ibe: InvalidBookException) {
            ResponseEntity.badRequest().body(ibe.message)
        }
    }

    @PutMapping(consumes = ["application/json"])
    fun updateBook(@RequestBody book: BookDTO): ResponseEntity<Any> {

        try {
            bookService.findById(book.id)
        } catch (enfe: EntityNotFoundException) {
            return ResponseEntity.badRequest().body(enfe.message)
        }

        bookService.save(book)
        return ResponseEntity.status(HttpStatus.ACCEPTED).build()
    }


}