package com.ottofernan.librarycrud.services.visitor;

import com.ottofernan.librarycrud.domain.models.Visitor;

public interface VisitorService {

     Visitor save(Visitor visitor);

     Visitor findById(Long id);

     Visitor findByFirstName(String firstName);

     Visitor findByFirstNameAndLastName(String firstName, String lastName);
}
