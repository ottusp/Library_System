package com.ottofernan.librarycrud.services.book;

import com.ottofernan.librarycrud.domain.dtos.BookDTO;
import com.ottofernan.librarycrud.domain.models.Book;
import com.ottofernan.librarycrud.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ottofernan.librarycrud.domain.dtos.BookDTOKt.toModel;

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
        return bookRepository
                .findByTitleIgnoreCaseLikeOrderByTitleAsc("%" + title + "%")
                .stream()
                .map(Book::toDto)
                .collect(Collectors.toSet());
    }

    public BookDTO findById(Long id){
        Book book = bookRepository.findById(id).orElse(null);
        if(book == null) return null;
        else return book.toDto();
    }

    public Set<BookDTO> findAll(){
        Set<BookDTO> books = new HashSet<>();
        bookRepository.findAll().forEach(book -> books.add(book.toDto()));
        return books;
    }

    public BookDTO save(BookDTO book){
        return bookRepository.save(toModel(book)).toDto();
    }
}
