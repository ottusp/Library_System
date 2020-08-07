package com.ottofernan.librarycrud.controllers;

import com.ottofernan.librarycrud.models.Book;
import com.ottofernan.librarycrud.models.Visitor;
import com.ottofernan.librarycrud.models.VisitorBook;
import com.ottofernan.librarycrud.services.restbook.RestBookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/books", "/books/"})
public class BookController {

    private final RestBookService restBookService;

    public BookController(RestBookService restBookService) {
        this.restBookService = restBookService;
    }

    @GetMapping({"/list", "/listBooks"})
    public String getBooks(Model model){
        model.addAttribute("books", restBookService.findAllBooks());
        model.addAttribute("newBook", new Book());

        return "books/listBooks";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute("title") String title, Model model){

        if(title == null) {
            model.addAttribute("title", "");
        } else{
            model.addAttribute("results", restBookService.findByTitle(title));
        }
        model.addAttribute("rented_book_id");
        return "books/searchResults";
    }

    @GetMapping("/returnBook")
    public String returnBook(@ModelAttribute Visitor visitor, Model model, RedirectAttributes redirect) {
        if(!Visitor.isValid(visitor)) {
            model.addAttribute("visitor", new Visitor());
            return "/visitors/login";
        } else {
            redirect.addFlashAttribute("visitor", new VisitorBook(visitor, new Book()));
            redirect.addFlashAttribute("successNextPage", "/visitors/returnAuthTrue");
            redirect.addFlashAttribute("failNextPage", "/visitors/returnAuthFail");
            return "redirect:/visitors/auth";
        }
    }

    @GetMapping("/donate")
    public String donate(@ModelAttribute("newBook") Book book, Model model){
        model.addAttribute("newBook", new Book());
        return "/books/donate";
    }

    @PostMapping("/executeDonate")
    public String executeDonate(@ModelAttribute("newBook") Book book){
        if(Book.isValid(book) && book.getAmount() > 0){
            System.out.println("Vou adicionar o livro");
            restBookService.create(book);
            return "/books/donateSuccessfully";
        }
        return "redirect:/books/donate";
    }

    @GetMapping("/rent")
    public String rentBook(@ModelAttribute("rented_book_id") Long id, Model model){

        if(id != null){
            Book rentedBook = restBookService.findById(id);
            model.addAttribute("rented_book", rentedBook);
            model.addAttribute("borrowing_visitor", new VisitorBook(new Visitor(), rentedBook));
        }
        return "/books/rentBook";
    }

    @PostMapping("/post")
    public String postBook(@ModelAttribute("newBook") Book book){

        restBookService.create(book);
        return "index";
    }

}
