package com.ottofernan.librarycrud.services.restbook;


import com.ottofernan.librarycrud.domain.dtos.BookDTO;
import com.ottofernan.librarycrud.domain.models.Book;

import java.util.List;

public interface RestBookService {

     List<BookDTO> findAllBooks();

     BookDTO create(BookDTO book);

     void update(BookDTO book);

     List<BookDTO> findByTitle(String title);

     BookDTO findById(Long id);
}
