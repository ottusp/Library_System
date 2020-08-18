package com.ottofernan.librarycrud.services.visitor;

import com.ottofernan.librarycrud.domain.dtos.VisitorDTO;
import com.ottofernan.librarycrud.domain.enums.Status;
import com.ottofernan.librarycrud.domain.models.VisitorBook;

public interface VisitorService {

     VisitorDTO save(VisitorDTO visitor);

     VisitorDTO findById(Long id);

     VisitorDTO findByFirstName(String firstName);

     VisitorDTO findByFirstNameAndLastName(String firstName, String lastName);

     Status auth (VisitorBook tested);
     
     Status rent (VisitorBook visitorBook);

     Status returnBook(VisitorBook visitorBook);
}
