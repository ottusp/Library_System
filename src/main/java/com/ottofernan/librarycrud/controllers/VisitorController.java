package com.ottofernan.librarycrud.controllers;

import com.ottofernan.librarycrud.domain.dtos.BookDTO;
import com.ottofernan.librarycrud.domain.models.Visitor;
import com.ottofernan.librarycrud.domain.models.VisitorBook;
import com.ottofernan.librarycrud.services.restbook.RestBookService;
import com.ottofernan.librarycrud.services.visitor.VisitorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.ottofernan.librarycrud.domain.dtos.BookDTOKt.toModel;

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
        System.out.println(visitorBook.getBook().getId());

        if(Visitor.isNotValid(visitor))
            return "redirect:http://localhost:8080/books/rentBook";

        redirectAttributes.addFlashAttribute("successNextPage", "/visitors/rentAuthTrue");
        redirectAttributes.addFlashAttribute("failNextPage", "/visitors/rentAuthFalse");
        redirectAttributes.addFlashAttribute("visitor", visitorBook);
        return "redirect:/visitors/auth";

    }

    @GetMapping("/rentAuthTrue")
    public String executeRentBook(HttpServletRequest request){

        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap == null)
            return "error_";

        VisitorBook visitorBook = (VisitorBook) inputFlashMap.get("visitor");
        BookDTO book = restBookService.findById(visitorBook.getBook().getId());
        System.out.println(visitorBook);
        Visitor visitor = visitorService.findByFirstName(visitorBook.getVisitor().getFirstName());
        if(toModel(book).rent(visitor)) {
            visitorService.save(visitor);
            restBookService.update(book);
            return "books/rentSuccessfully";
        } else {
            return "books/alreadyHaveBook";
        }
    }

    @GetMapping("/rentAuthFalse")
    public String failOnRent(){
        return "visitors/wrongPassword";
    }

    @GetMapping("/returnAuthTrue")
    public String returnSucessfully(HttpServletRequest request, Model model){
        Map<String, ?> input = RequestContextUtils.getInputFlashMap(request);
        if(input != null){
            VisitorBook visitorBook = (VisitorBook) input.get("visitor");
            Visitor checkedVisitor = visitorService.findByFirstName(visitorBook.getVisitor().getFirstName());
            visitorBook.setVisitor(checkedVisitor);
            model.addAttribute("visitor", visitorBook);
            return "/books/listVisitorsBooks";
        }
        return "error_";
    }

    @GetMapping("/returnAuthFail")
    public String failOnReturn(){
        return "visitors/wrongPassword";
    }

    @GetMapping("/executeReturn")
    public String executeReturn(@ModelAttribute("visitor") VisitorBook visitorBook, RedirectAttributes redirect){

        Visitor checkedVisitor = visitorService.findById(visitorBook.getVisitor().getId());
        BookDTO checkedBook = restBookService.findById(visitorBook.getBook().getId());

        if(checkedBook == null || checkedVisitor == null){
            redirect.addFlashAttribute("visitor", visitorBook);
            return "redirect:/visitors/returnAuthTrue";
        }

        if(toModel(checkedBook).returnBook(checkedVisitor)) {
            visitorService.save(checkedVisitor);
            restBookService.update(checkedBook);
        } else {
            redirect.addFlashAttribute("visitor", visitorBook);
            return "redirect:/visitors/returnAuthTrue";
        }

        return "redirect:/index";
    }

}
