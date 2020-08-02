package com.ottofernan.librarycrud.services.book;

import com.ottofernan.librarycrud.models.Book;

import java.util.Set;

public interface BookService {

    public Book findByTitle(String title);

    public Set<Book> findAllByTitle(String title);

    public Book findById(Long id);

    public Set<Book> findAll();

    public Book save(Book book);
}
