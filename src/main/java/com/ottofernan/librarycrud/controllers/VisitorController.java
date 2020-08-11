package com.ottofernan.librarycrud.controllers;

import com.ottofernan.librarycrud.domain.dtos.BookDTO;
import com.ottofernan.librarycrud.domain.dtos.VisitorDTO;
import com.ottofernan.librarycrud.domain.models.Book;
import com.ottofernan.librarycrud.domain.models.Visitor;
import com.ottofernan.librarycrud.domain.models.VisitorBook;
import com.ottofernan.librarycrud.services.restbook.RestBookService;
import com.ottofernan.librarycrud.services.visitor.VisitorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.ottofernan.librarycrud.domain.dtos.BookDTOKt.toModel;
import static com.ottofernan.librarycrud.domain.dtos.VisitorDTOKt.toModel;

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
    public String signInPage (@ModelAttribute("visitor") VisitorDTO visitor, Model model){

        if(visitor == null) {
            model.addAttribute("visitor", new Visitor());
            return "/visitors/signIn";
        }

        visitor.getFirstName();
        visitor.getLastName();
        visitorService.save(visitor);
        return "index";

    }
    

    @GetMapping("/auth")
    public String auth(HttpServletRequest request, RedirectAttributes redirect, Model model){

        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap == null) return "index";

        inputFlashMap.forEach(redirect::addFlashAttribute);
        VisitorBook visitor = (VisitorBook) inputFlashMap.get("visitor");
        VisitorDTO correctVisitor = visitorService.findByFirstName(visitor
                .getVisitor().getFirstName());

        if (correctVisitor == null) {
            model.addAttribute("visitor", new Visitor());
            return "/visitors/signIn";
        }

        String nextPage;
        if (Visitor.isPasswordCorrect(visitor.getVisitor(), correctVisitor))
            nextPage = (String) inputFlashMap.get("successNextPage");
        else
            nextPage = (String) inputFlashMap.get("failNextPage");

        return "redirect:" + nextPage;
    }

    @GetMapping("/rentBook")
    public String rentBook(@ModelAttribute VisitorBook visitorBook, RedirectAttributes redirectAttributes){
        Visitor visitor = toModel(visitorBook.getVisitor());

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
        BookDTO bookDTO = restBookService.findById(visitorBook.getBook().getId());
        Book book = toModel(bookDTO);
        VisitorDTO visitor = visitorService.findByFirstName(visitorBook.getVisitor().getFirstName());

        if(book.rent(visitor)) {
            visitorService.save(visitor);
            restBookService.update(book.toDto());
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
            VisitorDTO checkedVisitor = visitorService.findByFirstName(
                    visitorBook.getVisitor().getFirstName());

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

        VisitorDTO checkedVisitor = visitorService.findById(visitorBook.getVisitor().getId());
        BookDTO checkedBookDTO = restBookService.findById(visitorBook.getBook().getId());

        if(checkedBookDTO == null || checkedVisitor == null){
            redirect.addFlashAttribute("visitor", visitorBook);
            return "redirect:/visitors/returnAuthTrue";
        }

        Book checkedBook = toModel(checkedBookDTO);

        if(checkedBook.returnBook(checkedVisitor)) {
            visitorService.save(checkedVisitor);
            restBookService.update(checkedBook.toDto());
            return "redirect:/index";
        } else {
            redirect.addFlashAttribute("visitor", visitorBook);
            return "redirect:/visitors/returnAuthTrue";
        }

    }

}
