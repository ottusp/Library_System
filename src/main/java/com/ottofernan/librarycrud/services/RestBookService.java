package com.ottofernan.librarycrud.services;


import com.ottofernan.librarycrud.models.Book;

import java.util.List;
import java.util.Set;

public interface RestBookService {

    public Set<Book> findAllBooks();

//    public List<Book> findByName();

    public Book create(Book book);
}
