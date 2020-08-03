package com.ottofernan.librarycrud.services.visitor;

import com.ottofernan.librarycrud.models.Visitor;

public interface VisitorService {

     Visitor save(Visitor visitor);

     Visitor findById(Long id);

     Visitor findByFirstName(String firstName);
}
