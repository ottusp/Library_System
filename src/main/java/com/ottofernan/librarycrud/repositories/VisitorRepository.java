package com.ottofernan.librarycrud.repositories;

import com.ottofernan.librarycrud.domain.models.Visitor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorRepository extends CrudRepository<Visitor, Long> {

    Visitor findByFirstName(String name);

    Visitor findByFirstNameAndLastName(String firstName, String lastName);
}
