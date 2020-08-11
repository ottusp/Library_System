package com.ottofernan.librarycrud.domain.dtos

import com.ottofernan.librarycrud.domain.models.Author
import com.ottofernan.librarycrud.domain.models.Book
import com.ottofernan.librarycrud.domain.models.Visitor

data class BookDTO(
        val id: Long = 0,
        val title: String = "",
        val publisher: String = "",
        val isbn: String = "",
        val amount: Int = 0,
        val visitors: Set<Visitor> = setOf<Visitor>(),
        val authors: Set<Author> = setOf<Author>()
)

fun toModel(bookDTO: BookDTO): Book{
    return Book(
            bookDTO.id,
            bookDTO.title,
            bookDTO.publisher,
            bookDTO.isbn,
            bookDTO.amount,
            bookDTO.visitors,
            bookDTO.authors
    )
}