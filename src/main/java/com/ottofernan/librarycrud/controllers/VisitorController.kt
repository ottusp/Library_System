package com.ottofernan.librarycrud.controllers

import com.ottofernan.librarycrud.domain.dtos.VisitorDTO
import com.ottofernan.librarycrud.domain.dtos.toModel
import com.ottofernan.librarycrud.domain.models.Visitor
import com.ottofernan.librarycrud.domain.models.VisitorBook
import com.ottofernan.librarycrud.services.restbook.RestBookService
import com.ottofernan.librarycrud.services.visitor.VisitorService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.support.RequestContextUtils
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/visitors")
class VisitorController(
        private val visitorService: VisitorService,
        private val restBookService: RestBookService
) {

    @GetMapping("/signIn")
    fun signInPage(@ModelAttribute("visitor") visitor: VisitorDTO?, model: Model): String {
        if(visitor == null) {
            model.addAttribute("visitor", VisitorDTO())
            return "/visitors/signIn"
        }

        visitorService.save(visitor)
        return "redirect:/"

    }

    @GetMapping("/auth")
    fun auth (request: HttpServletRequest, redirect: RedirectAttributes, model: Model): String {

        val inputFlashMap = RequestContextUtils.getInputFlashMap(request) ?: return "redirect:/"
        inputFlashMap.forEach { (s: String?, o: Any?) -> redirect.addFlashAttribute(s, o) }

        val visitor = inputFlashMap["visitor"] as VisitorBook
        val correctVisitor = visitorService.findByFirstName(visitor.visitor.firstName)
        if(correctVisitor == null){
            model.addAttribute("visitor", VisitorDTO())
            return "visitors/signIn"
        }

        val nextPage: String
        nextPage = if (Visitor.isPasswordCorrect(visitor.visitor, correctVisitor))
            inputFlashMap["successNextPage"] as String
        else inputFlashMap["failNextPage"] as String

        return "redirect:$nextPage"
    }

    @GetMapping("/rentBook")
    fun rentBook(@ModelAttribute visitorBook: VisitorBook, redirect: RedirectAttributes): String {
        val visitor = visitorBook.visitor
        if(!Visitor.isValid(visitor)) return "redirect:http://localhost:8080/books/rentBook"

        redirect.addFlashAttribute("successNextPage", "/visitors/rentAuthTrue")
        redirect.addFlashAttribute("failNextPage", "/visitors/rentAuthFalse")
        redirect.addFlashAttribute("visitor", visitorBook)
        return "redirect:/visitors/auth"
    }

    @GetMapping("/rentAuthTrue")
    fun executeRentBook(request: HttpServletRequest): String {
        val inputFlashMap = RequestContextUtils.getInputFlashMap(request) ?: return "error_"

        val visitorBook = inputFlashMap["visitor"] as VisitorBook
        val bookDTO = restBookService.findById(visitorBook.book.id)
        val visitor = visitorService.findByFirstName((visitorBook.visitor.firstName))
        val book = toModel(bookDTO)

        if(!book.rent(visitor)) return "books/alreadyHaveBook"

        visitorService.save(visitor)
        restBookService.update(book.toDto())
        return "books/rentSuccessfully"
    }

    @GetMapping("/rentAuthFalse")
    fun failOnRent(): String =
        "visitors/wrongPassword"

    @GetMapping("/returnAuthTrue")
    fun returnSuccessfully (request: HttpServletRequest, model: Model): String{
        val input = RequestContextUtils.getInputFlashMap(request) ?: return "error_"
        val visitorBook = input["visitor"] as VisitorBook
        val checkedVisitor = visitorService.findByFirstName(visitorBook.visitor.firstName)
        visitorBook.visitor = checkedVisitor
        model.addAttribute("visitor", visitorBook)
        return "/books/listVisitorsBooks"

    }

    @GetMapping("/returnAuthFail")
    fun failOnReturn(): String =
            "visitors/wrongPassword"

    @GetMapping("/executeReturn")
    fun executeReturn(@ModelAttribute("visitor") visitorBook: VisitorBook, redirect: RedirectAttributes): String {
        val checkedVisitor = visitorService.findById(visitorBook.visitor.id)
        val checkedBookDTO = restBookService.findById(visitorBook.book.id)
        val checkedBook = toModel(checkedBookDTO)

        if(checkedVisitor == null) {
            redirect.addFlashAttribute("visitor", visitorBook)
            return "redirect:/visitors/returnAuthTrue"
        }

        return if(checkedBook.returnBook(checkedVisitor)) {
            visitorService.save(checkedVisitor)
            restBookService.update(checkedBook.toDto())
            "redirect:/"
        } else {
            redirect.addFlashAttribute("visitor", visitorBook)
            "redirect:/visitors/returnAuthTrue"
        }
    }
}