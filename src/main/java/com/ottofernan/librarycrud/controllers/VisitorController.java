package com.ottofernan.librarycrud.controllers;

import com.ottofernan.librarycrud.models.Visitor;
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

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping("/signIn")
    public String signInPage (@ModelAttribute("visitor") Visitor visitor, Model model){

        if(visitor == null) {
            model.addAttribute("visitor", new Visitor());
            return "/visitors/signIn";
        } else{
            visitorService.save(visitor);
            return "/";
        }

    }
}
