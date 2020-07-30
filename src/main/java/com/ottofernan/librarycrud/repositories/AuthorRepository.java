package com.ottofernan.librarycrud.repositories;

import com.ottofernan.librarycrud.models.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {

    Author findByFirstName(String name);
}
