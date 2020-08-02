package com.ottofernan.librarycrud.models;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Visitor extends Person{

    private String password;

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
    public String toString() {
        String string = super.toString();
        string += "Visitor{" +
                "password='" + password + '\'' +
                ", books=" + books +
                '}';
        return string;
    }
}
