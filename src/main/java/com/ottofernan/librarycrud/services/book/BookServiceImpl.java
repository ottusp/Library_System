package com.ottofernan.librarycrud.services.book;

import com.ottofernan.librarycrud.models.Book;
import com.ottofernan.librarycrud.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book findByTitle(String title){
        return bookRepository.findByTitle(title);
    }

    public Set<Book> findAllByTitle(String title){
        System.out.println("In findAllByTitle, will search for = '" + title + "'");
        return bookRepository.findByTitleIgnoreCaseLikeOrderByTitleAsc("%" + title + "%");
    }

    public Book findById(Long id){
        return bookRepository.findById(id).orElse(null);
    }

    public Set<Book> findAll(){
        Set<Book> books = new HashSet<>();
        bookRepository.findAll().forEach(books::add);
        return books;
    }

    public Book save(Book book){
        return bookRepository.save(book);
    }
}
