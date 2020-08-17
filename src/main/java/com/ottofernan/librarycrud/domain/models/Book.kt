//package com.ottofernan.librarycrud.domain.models
//
//import com.ottofernan.librarycrud.domain.dtos.BookDTO
//import com.ottofernan.librarycrud.domain.dtos.VisitorDTO
//import com.ottofernan.librarycrud.domain.dtos.toModel
//import java.util.stream.Collectors
//import javax.persistence.*
//
//@Entity
//@Table(name = "books")
//class Book(
//        var id: Long = 0,
//        var title: String = "",
//        var publisher: String = "",
//        var isbn: String = "",
//        var amount: Int = 0,
//
//        @ManyToMany
//        @JoinTable(name = "author_book",
//                joinColumns = [JoinColumn(name = "book_id", referencedColumnName = "id")],
//                inverseJoinColumns = [JoinColumn(name = "author_id", referencedColumnName = "id")])
//        var authors: MutableSet<Author> = HashSet()
//): BaseEntity(id) {
//
//    @ManyToMany
//    @JoinTable(name = "visitor_book",
//            joinColumns = [JoinColumn(name = "book_id", referencedColumnName = "id")],
//            inverseJoinColumns = [JoinColumn(name = "visitor_id", referencedColumnName = "id")])
//    var visitors: MutableSet<Visitor> = mutableSetOf()
//
//    override fun toString(): String {
//        return "Book{" +
//                "title='" + title + '\'' +
//                '}'
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other == null || javaClass != other.javaClass) return false
//        val book = other as Book
//        return id == book.id
//    }
//
//    fun rent(visitor: Visitor): Boolean {
//        val amount: Int = this.amount
//        val containsBook: Boolean = visitor.containBook(this)
//        if (amount > 0 && !containsBook) {
//            visitor.books.add(this)
//            this.visitors.add(visitor)
//            this.amount = amount - 1
//            return true
//        }
//        return false
//    }
//
//    fun rent(visitorDTO: VisitorDTO): Boolean {
//        val visitor = toModel(visitorDTO)
//        val isValid = rent(visitor)
//        if (isValid) {
//            visitorDTO.books = visitor.books.stream().map { obj: Book -> obj.toDto() }.collect(Collectors.toSet())
//            return true
//        }
//        return false
//    }
//
//    fun returnBook(visitor: Visitor): Boolean {
//        if (visitor.containBook(this)) {
//            val isBookRemoved = visitor.getBooks().removeIf { book: Book -> book == this }
//            val isVisitorRemoved: Boolean = this.visitors.removeIf { visit: Visitor -> visit == visitor }
//            val amount: Int = this.amount
//            this.amount = amount + 1
//            return isBookRemoved && isVisitorRemoved
//        }
//        return false
//    }
//
//    fun returnBook(visitorDTO: VisitorDTO): Boolean {
//        val visitor = toModel(visitorDTO)
//        val isValid = returnBook(visitor)
//        if (isValid) {
//            visitorDTO.books = visitor.books.stream().map { obj: Book -> obj.toDto() }.collect(Collectors.toSet())
//            return true
//        }
//        return false
//    }
//
//    fun toDto(): BookDTO? {
//        val bookDTO = BookDTO(
//                id, title, publisher, isbn, amount, authors
//        )
//        for (visitor in visitors) {
//            val visitorDTO = VisitorDTO(
//                    visitor.id,
//                    visitor.firstName,
//                    visitor.lastName,
//                    visitor.password
//            )
//            visitorDTO.books.add(bookDTO)
//            bookDTO.visitors.add(visitorDTO)
//        }
//        return bookDTO
//    }
//
//    companion object Validation {
//
//        fun isValid(book: Book?): Boolean {
//            if (book == null) return false
//            return !(book.title.isEmpty() || book.publisher.isEmpty()
//                    || book.isbn.isEmpty())
//        }
//
//        fun isValid(bookDTO: BookDTO?): Boolean {
//            val book = BookDTO.toModel(bookDTO)
//            return isValid(book)
//        }
//
//    }
//
//}