package com.ottofernan.librarycrud.domain.models;

import com.ottofernan.librarycrud.domain.dtos.BookDTO;
import com.ottofernan.librarycrud.domain.dtos.VisitorDTO;

public class VisitorBook {
    private VisitorDTO visitor;
    private BookDTO book;

    public VisitorBook(VisitorDTO visitor, BookDTO book) {
        this.visitor = visitor;
        this.book = book;
    }

    public VisitorDTO getVisitor() {
        return visitor;
    }

    public void setVisitor(VisitorDTO visitor) {
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
