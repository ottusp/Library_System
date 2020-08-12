package com.ottofernan.librarycrud.controllers

import com.ottofernan.librarycrud.domain.dtos.BookDTO
import com.ottofernan.librarycrud.domain.dtos.VisitorDTO
import com.ottofernan.librarycrud.domain.dtos.toModel
import com.ottofernan.librarycrud.domain.models.Book
import com.ottofernan.librarycrud.domain.models.Visitor
import com.ottofernan.librarycrud.domain.models.VisitorBook
import com.ottofernan.librarycrud.services.restbook.RestBookService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/books")
class BookController (private val restBookService: RestBookService) {

    @GetMapping("list")
    fun getBooks(model: Model): String {
        model.addAttribute("books", restBookService.findAllBooks())
        model.addAttribute("newBook", BookDTO())

        return "books/listBooks"
    }

    @GetMapping("search")
    fun search(@ModelAttribute("title") title: String, model: Model): String{
        model.addAttribute("results", restBookService.findByTitle(title))
        model.addAttribute("rented_book_id")
        return "books/searchResults"
    }

    @GetMapping("/returnBook")
    fun returnBook(@ModelAttribute visitor: VisitorDTO, model: Model, redirect: RedirectAttributes): String {
        return if(Visitor.isNotValid(visitor)) {
            model.addAttribute("visitor", VisitorDTO())
            "/visitors/login"
        } else {
            redirect.addFlashAttribute("visitor", VisitorBook(visitor, BookDTO()))
            redirect.addFlashAttribute("successNextPage", "/visitors/returnAuthTrue")
            redirect.addFlashAttribute("failNextPage", "/visitors/returnAuthFail")
            "redirect:/visitors/auth"
        }
    }

    @GetMapping("/donate")
    fun donate(@ModelAttribute("newBook") book: BookDTO, model: Model): String{
        model.addAttribute("newBook", BookDTO())
        return "/books/donate"
    }

    @PostMapping("executeDonate")
    fun executeDonate(@ModelAttribute("newBook") bookDTO: BookDTO): String{
        val book = toModel(bookDTO)
        if(Book.isValid(book) && book.amount > 0){
            restBookService.create(bookDTO)
            return "/books/donateSuccessfully"
        }
        return "redirect:/books/donate"
    }

    @GetMapping("/rent")
    fun rentBook(@ModelAttribute("rented_book_id") id: Long, model: Model): String {
        val rentedBook = restBookService.findById(id)
        model.addAttribute("rented_book", rentedBook)
        model.addAttribute("borrowing_visitor", VisitorBook(VisitorDTO(), rentedBook))

        return "/books/rentBook"
    }

    @PostMapping("/post")
    fun postBook(@ModelAttribute("newBook") book: BookDTO): String {
        restBookService.create(book)
        return "index"
    }
}