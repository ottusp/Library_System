package com.ottofernan.librarycrud.controllers

import com.ottofernan.librarycrud.domain.dtos.BookDTO
import com.ottofernan.librarycrud.domain.dtos.toModel
import com.ottofernan.librarycrud.domain.models.Book
import com.ottofernan.librarycrud.services.book.BookService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookRestController(private val bookService: BookService) {

    @GetMapping("/get")
    fun getAll(): Set<BookDTO> =
            bookService.findAll()

    @GetMapping("/get/{id}")
    fun getById(@PathVariable("id") id: Long) =
            bookService.findById(id)

    @GetMapping("/getByTitle")
    fun getByTitle(@ModelAttribute("title") title: String): Set<BookDTO> =
            bookService.findAllByTitle(title)

    @PostMapping
    fun postBook(@RequestBody bookDTO: BookDTO): BookDTO {
        val book = toModel(bookDTO)
        if(Book.isValid(book)){
            bookService.save(bookDTO)
        }
        return bookDTO
    }

    @PutMapping(consumes = ["application/json"])
    fun updateBook(@RequestBody book: BookDTO) {
        val checkedBook = bookService.findById(book.id)
        if(checkedBook != null) {
            bookService.save(book)
        }
    }


}