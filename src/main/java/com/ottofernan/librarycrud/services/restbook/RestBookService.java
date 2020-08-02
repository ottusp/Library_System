package com.ottofernan.librarycrud.services.restbook;


import com.ottofernan.librarycrud.models.Book;

import java.util.List;

public interface RestBookService {

     List<Book> findAllBooks();

     Book create(Book book);

     List<Book> findByTitle(String title);

     Book findById(Long id);
}
