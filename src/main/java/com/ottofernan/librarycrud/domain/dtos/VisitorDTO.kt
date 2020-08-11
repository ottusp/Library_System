package com.ottofernan.librarycrud.domain.dtos

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ottofernan.librarycrud.domain.models.Book
import com.ottofernan.librarycrud.domain.models.Visitor

data class VisitorDTO(
        var id: Long = 0,
        var firstName: String = "",
        var lastName: String = "",
        var password: String = "",
){
        @JsonIgnore
        var books: MutableSet<BookDTO> = HashSet()
}

fun toModel(visitorDTO: VisitorDTO): Visitor {
        val visitor = Visitor(
                visitorDTO.id,
                visitorDTO.firstName,
                visitorDTO.lastName,
                visitorDTO.password,
        )
        visitorDTO.books.forEach{
                val book = Book(it.id, it.title, it.publisher, it.isbn, it.amount, it.authors)
                book.visitors.add(visitor)
                visitor.books.add(book)
        }

        return visitor
}