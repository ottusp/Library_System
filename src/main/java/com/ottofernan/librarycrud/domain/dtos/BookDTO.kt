package com.ottofernan.librarycrud.domain.dtos

import com.ottofernan.librarycrud.domain.models.Author
import com.ottofernan.librarycrud.domain.models.Book
import com.ottofernan.librarycrud.domain.models.Visitor

data class BookDTO(
        var id: Long = 0,
        var title: String = "",
        var publisher: String = "",
        var isbn: String = "",
        var amount: Int = 0,
        var authors: MutableSet<Author> = HashSet()
) {
    var visitors: MutableSet<VisitorDTO> = HashSet()
}

fun toModel(bookDTO: BookDTO): Book{
    val book = Book(
            bookDTO.id, bookDTO.title, bookDTO.publisher, bookDTO.isbn, bookDTO.amount, bookDTO.authors
    )
    for (visitor in bookDTO.visitors) {
        val visitor = Visitor(
                visitor.id,
                visitor.firstName,
                visitor.lastName,
                visitor.password
        )
        visitor.books.add(book)
        book.visitors.add(visitor)
    }

    return book
}