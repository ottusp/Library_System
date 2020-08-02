package com.ottofernan.librarycrud.services.restbook;


import com.ottofernan.librarycrud.models.Book;

import java.util.List;

public interface RestBookService {

    public List<Book> findAllBooks();

    public Book create(Book book);

    public List<Book> findByTitle(String title);
}
