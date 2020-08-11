package com.ottofernan.librarycrud.domain.models;


import com.ottofernan.librarycrud.domain.dtos.BookDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Table;

@Entity
@Table(name = "books")
public class Book extends BaseEntity{

    private String title = "";
    private String publisher = "";
    private String isbn = "";
    private Integer amount = 0;

    @ManyToMany
    @JoinTable(name = "visitor_book", joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "visitor_id"))
    private Set<Visitor> visitors = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "author_book", joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();

    public Book() {}

    public Book(String title, String publisher) {
        this.title = title;
        this.publisher = publisher;
    }

    public Book(Long id, String title, String publisher, String isbn, Integer amount,
                Set<Visitor> visitors, Set<Author> authors) {
        setId(id);
        this.title = title;
        this.publisher = publisher;
        this.isbn = isbn;
        this.amount = amount;
        this.visitors = visitors;
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Set<Visitor> getVisitors() {
        return visitors;
    }

    public void setVisitors(Set<Visitor> visitors) {
        this.visitors = visitors;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return this.getId().equals(book.getId());
    }

    public boolean rent(Visitor visitor){
        int amount = this.getAmount();
        boolean containsBook = visitor.containBook(this);
        if(amount > 0 && !containsBook){
            visitor.getBooks().add(this);
            this.getVisitors().add(visitor);
            this.setAmount(amount - 1);
            return true;
        }
        return false;
    }

    public boolean returnBook(Visitor visitor){
        if(visitor.containBook(this)) {
            boolean isBookRemoved = visitor.getBooks().removeIf(book -> book.equals(this));
            boolean isVisitorRemoved = this.getVisitors().removeIf(visit -> visit.equals(visitor));
            int amount = this.getAmount();
            this.setAmount(amount + 1);
            return isBookRemoved && isVisitorRemoved;
        }
        return false;
    }

    public BookDTO toDto(){
        return new BookDTO(
            getId(), title, publisher, isbn, amount, visitors, authors
        );
    }

    public static boolean isValid(Book book){
        if(book == null) return false;
        if(book.getTitle() != null && book.getPublisher() != null
                && book.getIsbn() != null){
            return !(book.getTitle().isEmpty() || book.getPublisher().isEmpty()
                    || book.getIsbn().isEmpty());
        }
        return false;
    }

    public static boolean isValidWithId(Book book){
        if(book != null){
            return Book.isValid(book) && book.getId() != null;
        } else return false;
    }
}
