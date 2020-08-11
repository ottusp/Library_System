package com.ottofernan.librarycrud.services.book;

import com.ottofernan.librarycrud.domain.dtos.BookDTO;
import com.ottofernan.librarycrud.domain.models.Book;

import java.util.Set;

public interface BookService {

    BookDTO findByTitle(String title);

    Set<BookDTO> findAllByTitle(String title);

    BookDTO findById(Long id);

    Set<BookDTO> findAll();

    BookDTO save(BookDTO book);
}
