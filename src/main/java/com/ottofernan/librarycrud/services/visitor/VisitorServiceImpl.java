package com.ottofernan.librarycrud.services.visitor;

import com.ottofernan.librarycrud.domain.dtos.VisitorDTO;
import com.ottofernan.librarycrud.domain.models.Visitor;
import com.ottofernan.librarycrud.repositories.VisitorRepository;
import org.springframework.stereotype.Service;

import static com.ottofernan.librarycrud.domain.dtos.VisitorDTOKt.toModel;

@Service
public class VisitorServiceImpl implements VisitorService {

    private final VisitorRepository visitorRepository;

    public VisitorServiceImpl(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public VisitorDTO save(VisitorDTO visitor){
        Visitor savedVisitor = visitorRepository.save(toModel(visitor));
        return savedVisitor.toDto();
    }

    public VisitorDTO findById(Long id){
        Visitor visitor = visitorRepository.findById(id).orElse(null);
        if(visitor == null) return null;
        return visitor.toDto();
    }

    public VisitorDTO findByFirstName(String firstName){
        Visitor visitor = visitorRepository.findByFirstName(firstName);
        if(visitor == null) return null;
        return visitor.toDto();
    }

    public VisitorDTO findByFirstNameAndLastName(String firstName, String lastName){
        return visitorRepository.findByFirstNameAndLastName(firstName, lastName).toDto();
    }
}
