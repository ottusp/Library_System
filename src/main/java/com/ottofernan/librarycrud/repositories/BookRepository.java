package com.ottofernan.librarycrud.repositories;

import com.ottofernan.librarycrud.models.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    Book findByTitle(String name);

    Set<Book> findByTitleLike(String title);

}
