package com.ottofernan.librarycrud.services;


import com.ottofernan.librarycrud.models.Book;

import java.util.Set;

public interface RestBookService {

    public Set<Book> findAllBooks();

    public Book create(Book book);

    public Set<Book> findByTitle(String title);
}
