package com.ottofernan.librarycrud.models;

public class VisitorBook {
    private Visitor visitor;
    private Book book;

    public VisitorBook(Visitor visitor, Book book) {
        this.visitor = visitor;
        this.book = book;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
