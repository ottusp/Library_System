package com.ottofernan.librarycrud.services.visitor;

import com.ottofernan.librarycrud.domain.dtos.VisitorDTO;
import com.ottofernan.librarycrud.domain.models.Visitor;

public interface VisitorService {

     VisitorDTO save(VisitorDTO visitor);

     VisitorDTO findById(Long id);

     VisitorDTO findByFirstName(String firstName);

     VisitorDTO findByFirstNameAndLastName(String firstName, String lastName);
}
