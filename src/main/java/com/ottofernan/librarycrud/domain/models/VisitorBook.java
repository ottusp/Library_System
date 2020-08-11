package com.ottofernan.librarycrud.domain.models;

import com.ottofernan.librarycrud.domain.dtos.BookDTO;

public class VisitorBook {
    private Visitor visitor;
    private BookDTO book;

    public VisitorBook(Visitor visitor, BookDTO book) {
        this.visitor = visitor;
        this.book = book;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "VisitorBook{" +
                "visitor=" + visitor +
                ", book=" + book +
                '}';
    }
}
