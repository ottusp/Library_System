package com.ottofernan.librarycrud.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ottofernan.librarycrud.domain.dtos.BookDTO;
import com.ottofernan.librarycrud.domain.dtos.VisitorDTO;

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

    public Visitor() {
    }

    public Visitor(Long id, String firstName, String lastName, String password) {
        super(id, firstName, lastName);
        this.password = password;
    }

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

    public VisitorDTO toDto(){
        VisitorDTO visitorDTO = new VisitorDTO(
                getId(), getFirstName(), getLastName(), password
        );
        for(Book book: books){
            BookDTO bookDTO = new BookDTO(
                    book.getId(),
                    book.getTitle(),
                    book.getPublisher(),
                    book.getIsbn(),
                    book.getAmount(),
                    book.getAuthors()
            );
            visitorDTO.getBooks().add(bookDTO);
            bookDTO.getVisitors().add(visitorDTO);
        }

        return visitorDTO;
    }

    public boolean containBook(String title){
        return getBooks().stream().anyMatch(book -> book.getTitle().equals(title));
    }

    public boolean containBook(Book book){
        return getBooks().stream().anyMatch(other -> other.equals(book));
    }

    public static boolean isPasswordCorrect(VisitorDTO received, VisitorDTO correct){
        if(received == null || correct == null) return false;
        return correct.getPassword().equals(received.getPassword());
    }

    public static boolean isValid(Visitor visitor){
        try{
            return !(visitor.getPassword().isEmpty() || visitor.getFirstName().isEmpty());
        } catch (NullPointerException npe){
            return false;
        }
    }

    public static boolean isValid(VisitorDTO visitorDTO){
        return isValid(VisitorDTO.toModel(visitorDTO));
    }

    public static boolean isNotValid(Visitor visitor){
        return !isValid(visitor);
    }

    public static boolean isNotValid(VisitorDTO visitor){
        return !isValid(visitor);
    }
}
