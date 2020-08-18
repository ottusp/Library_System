package com.ottofernan.librarycrud.controllers

import com.ottofernan.librarycrud.domain.dtos.VisitorDTO
import com.ottofernan.librarycrud.domain.enums.Status
import com.ottofernan.librarycrud.domain.models.Visitor
import com.ottofernan.librarycrud.domain.models.VisitorBook
import com.ottofernan.librarycrud.services.visitor.VisitorService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/visitors")
class VisitorController(
        private val visitorService: VisitorService,
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
    fun auth (redirect: RedirectAttributes, model: Model): String {
        model.asMap().forEach { (s: String?, o: Any?) -> redirect.addFlashAttribute(s, o) }

        val visitor = model.getAttribute("visitor") as VisitorBook
        val status = visitorService.auth(visitor)

        val nextPage = when(status) {
            Status.NOT_FOUND, null -> {
                model.addAttribute("visitor", VisitorDTO())
                return "/visitors/signIn"
            }
            Status.FAIL -> model.getAttribute("failNextPage") as String
            Status.SUCCESS -> model.getAttribute("successNextPage") as String
        }
        return "redirect:$nextPage"
    }

    @GetMapping("/rentBook")
    fun rentBook(@ModelAttribute visitorBook: VisitorBook, redirect: RedirectAttributes): String {
        val visitor = visitorBook.visitor
        if(Visitor.isNotValid(visitor)) return "redirect:http://localhost:8080/books/rentBook"

        redirect.addFlashAttribute("successNextPage", "/visitors/rentAuthTrue")
        redirect.addFlashAttribute("failNextPage", "/visitors/rentAuthFalse")
        redirect.addFlashAttribute("visitor", visitorBook)
        return "redirect:/visitors/auth"
    }

    @GetMapping("/rentAuthTrue")
    fun executeRentBook(model: Model): String {
        val visitorBook = model.asMap()["visitor"] as VisitorBook
        val status = visitorService.rent(visitorBook)

        return when(status) {
            Status.SUCCESS -> "books/rentSuccessfully"
            else -> "books/alreadyHaveBook"
        }
    }

    @GetMapping("/rentAuthFalse")
    fun failOnRent(): String =
        "visitors/wrongPassword"

    @GetMapping("/returnAuthTrue")
    fun returnSuccessfully (model: Model): String{
        val visitorBook = model.asMap()["visitor"] as VisitorBook
        val checkedVisitor = visitorService.findByFirstName(visitorBook.visitor.firstName)
        visitorBook.visitor = checkedVisitor
        model.addAttribute("visitor", visitorBook)
        return "/books/listVisitorsBooks"

    }

    @GetMapping("/returnAuthFail")
    fun failOnReturn(): String =
            "visitors/wrongPassword"

    @GetMapping("/executeReturn")
    fun executeReturn(
            @ModelAttribute("visitor") visitorBook: VisitorBook,
            redirect: RedirectAttributes
    ): String {
        val status = visitorService.returnBook(visitorBook)

        return when(status) {
            Status.SUCCESS -> "redirect:/"
            Status.FAIL, Status.NOT_FOUND, null -> {
                redirect.addFlashAttribute("visitor", visitorBook)
                "redirect:/visitors/returnAuthTrue"
            }
        }

    }
}