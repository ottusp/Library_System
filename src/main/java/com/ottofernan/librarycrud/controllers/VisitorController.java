package com.ottofernan.librarycrud.controllers;

import com.ottofernan.librarycrud.models.Book;
import com.ottofernan.librarycrud.models.Visitor;
import com.ottofernan.librarycrud.models.VisitorBook;
import com.ottofernan.librarycrud.services.restbook.RestBookService;
import com.ottofernan.librarycrud.services.visitor.VisitorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/visitors")
public class VisitorController {

    private final VisitorService visitorService;
    private final RestBookService restBookService;

    public VisitorController(VisitorService visitorService, RestBookService restBookService) {
        this.visitorService = visitorService;
        this.restBookService = restBookService;
    }

    @GetMapping("/signIn")
    public String signInPage (@ModelAttribute("visitor") Visitor visitor, Model model){

        if(visitor == null) {
            model.addAttribute("visitor", new Visitor());
        } else if(visitor.getFirstName() != null && visitor.getLastName() != null){
            visitorService.save(visitor);
            return "index";
        }

        return "/visitors/signIn";
    }

    @GetMapping("/auth")
    public String auth(@ModelAttribute("borrowing_visitor") VisitorBook visitorBook, Model model){

        try {
            Visitor visitor = visitorService.findByFirstName(visitorBook.getVisitor().getFirstName());
            Book rentedBook = restBookService.findById(visitorBook.getBook().getId());

            if(visitor == null){
                model.addAttribute("visitor", new Visitor());
                return "visitors/signIn";
            }

            if(visitor.getPassword().equals(visitorBook.getVisitor().getPassword())) {
                rentedBook.rent(visitor);
                visitorService.save(visitor);
                restBookService.update(rentedBook);
                return "index";
            }
            else {
                model.addAttribute("rented_book", rentedBook);
                model.addAttribute("borrowing_visitor", visitorBook);
                return "books/rentBook";
            }
        } catch (NullPointerException npe){
            return "books/rentBook";
        }
    }
}
