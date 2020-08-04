package com.ottofernan.librarycrud.controllers;

import com.ottofernan.librarycrud.models.Book;
import com.ottofernan.librarycrud.models.Visitor;
import com.ottofernan.librarycrud.models.VisitorBook;
import com.ottofernan.librarycrud.services.restbook.RestBookService;
import com.ottofernan.librarycrud.services.visitor.VisitorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
    public String auth(HttpServletRequest request, RedirectAttributes redirect, Model model){
        System.out.println("Estou na auth");

        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap != null) {
            inputFlashMap.forEach(redirect::addFlashAttribute);
            VisitorBook visitor = (VisitorBook) inputFlashMap.get("visitor");
            Visitor correctVisitor = visitorService.findByFirstName(visitor
                    .getVisitor().getFirstName());

            if (Visitor.isPasswordCorrect(visitor.getVisitor(), correctVisitor)) {
                String nextPage = (String) inputFlashMap.get("successNextPage");
                return "redirect:" + nextPage;
            } else if (correctVisitor == null) {
                model.addAttribute("visitor", new Visitor());
                return "/visitors/signIn";
            } else {
                String nextPage = (String) inputFlashMap.get("failNextPage");
                return "redirect:" + nextPage;
            }
        }

        return "index";
    }

    @GetMapping("/rentBook")
    public String rentBook(@ModelAttribute VisitorBook visitorBook, RedirectAttributes redirectAttributes){
        Visitor visitor = visitorBook.getVisitor();
        if(Visitor.isValid(visitor)){
            redirectAttributes.addFlashAttribute("successNextPage", "/visitors/rentAuthTrue");
            redirectAttributes.addFlashAttribute("failNextPage", "/visitors/rentAuthFalse");
            redirectAttributes.addFlashAttribute("visitor", visitorBook);
            return "redirect:auth";
        } else {
            return "redirect:http://localhost:8080/books/rentBook";
        }
    }

    @GetMapping("/rentAuthTrue")
    public String executeRentBook(HttpServletRequest request){
        System.out.println("Estou na rentAuthTrue");

        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap != null){
            VisitorBook visitorBook = (VisitorBook) inputFlashMap.get("visitor");
            Book book = restBookService.findById(visitorBook.getBook().getId());
            Visitor visitor = visitorService.findByFirstName(visitorBook.getVisitor().getFirstName());
            book.rent(visitor);
            visitorService.save(visitor);
            restBookService.update(book);
            return "books/rentSuccessfully";
        }
        return "error";
    }

    @GetMapping("/rentAuthFalse")
    public String failOnRent(){
        return "visitors/wrongPassword";
    }

}
