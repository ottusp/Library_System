package com.ottofernan.librarycrud.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Visitor extends Person{

    private String password;

    @JsonIgnore
    @ManyToMany(mappedBy = "visitors")
    Set<Book> books = new HashSet<>();

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Visitor visitor = (Visitor) o;

        return this.getId().equals(visitor.getId());
    }

   @Override
    public String toString() {
        String string = super.toString();
        string += "Visitor{" +
                "password='" + password + '\'' +
                ", books=" + books +
                '}';
        return string;
    }

    public boolean containBook(String title){
        return getBooks().stream().anyMatch(book -> book.getTitle().equals(title));
    }

    public boolean containBook(Book book){
        return getBooks().stream().anyMatch(other -> other.equals(book));
    }

}
