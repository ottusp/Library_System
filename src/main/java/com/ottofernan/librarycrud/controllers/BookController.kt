package com.ottofernan.librarycrud.controllers

import com.ottofernan.librarycrud.models.Book
import com.ottofernan.librarycrud.models.Visitor
import com.ottofernan.librarycrud.models.VisitorBook
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
class BookController (val restBookService: RestBookService) {

    @GetMapping("list")
    fun getBooks(model: Model): String {
        model.addAttribute("books", restBookService.findAllBooks())
        model.addAttribute("newBook", Book())

        return "books/listBooks"
    }

    @GetMapping("search")
    fun search(@ModelAttribute("title") title: String, model: Model): String{
        model.addAttribute("results", restBookService.findByTitle(title))
        model.addAttribute("rented_book_id")
        return "books/searchResults"
    }

    @GetMapping("/returnBook")
    fun returnBook(@ModelAttribute visitor: Visitor, model: Model, redirect: RedirectAttributes): String {
        return if(!Visitor.isValid(visitor)) {
            model.addAttribute("visitor", Visitor())
            "/visitors/login"
        } else {
            redirect.addFlashAttribute("visitor", VisitorBook(visitor, Book()))
            redirect.addFlashAttribute("successNextPage", "/visitors/returnAuthTrue")
            redirect.addFlashAttribute("failNextPage", "/visitors/returnAuthFail")
            "redirect:/visitors/auth"
        }
    }

    @GetMapping("/donate")
    fun donate(@ModelAttribute("newBook") book: Book, model: Model): String{
        model.addAttribute("newBook", Book())
        return "/books/donate"
    }

    @PostMapping("executeDonate")
    fun executeDonate(@ModelAttribute("newBook") book: Book): String{
        if(Book.isValid(book) && book.amount > 0){
            restBookService.create(book)
            return "/books/donateSuccessfully"
        }
        return "redirect:/books/donate"
    }

    @GetMapping("/rent")
    fun rentBook(@ModelAttribute("rented_book_id") id: Long, model: Model): String {
        val rentedBook = restBookService.findById(id)
        model.addAttribute("rented_book", rentedBook)
        model.addAttribute("borrowing_visitor", VisitorBook(Visitor(), rentedBook))

        return "/books/rentBook"
    }

    @PostMapping("/post")
    fun postBook(@ModelAttribute("newBook") book: Book): String {
        restBookService.create(book)
        return "index"
    }
}