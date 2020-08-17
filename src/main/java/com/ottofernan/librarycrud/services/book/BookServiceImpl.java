package com.ottofernan.librarycrud.services.book;

import com.ottofernan.librarycrud.domain.dtos.BookDTO;
import com.ottofernan.librarycrud.domain.exceptions.EntityNotFoundException;
import com.ottofernan.librarycrud.domain.exceptions.InvalidBookException;
import com.ottofernan.librarycrud.domain.models.Book;
import com.ottofernan.librarycrud.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookDTO findByTitle(String title){
        return bookRepository.findByTitle(title).toDto();
    }

    public Set<BookDTO> findAllByTitle(String title){
        Set<BookDTO> books = bookRepository
                .findByTitleIgnoreCaseLikeOrderByTitleAsc("%" + title + "%")
                .stream()
                .map(Book::toDto)
                .collect(Collectors.toSet());

        if(books.isEmpty())
            throw new EntityNotFoundException("No book matches title = " + title);

        return books;
    }

    public BookDTO findById(Long id){
        Book book = bookRepository.findById(id).orElse(null);
        if(book != null) return book.toDto();
        else throw new EntityNotFoundException("Could not find a book with id = " + id);
    }

    public Set<BookDTO> findAll(){
        Set<BookDTO> books = new HashSet<>();
        bookRepository.findAll().forEach(book -> books.add(book.toDto()));
        return books;
    }

    public BookDTO save(BookDTO book){
        if(Book.isValid(book))
            return bookRepository.save(BookDTO.Transform.toModel(book)).toDto();

        throw new InvalidBookException("The book " + book + " is invalid");
    }
}
