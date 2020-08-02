package com.ottofernan.librarycrud.services.visitor;

import com.ottofernan.librarycrud.models.Visitor;
import com.ottofernan.librarycrud.repositories.VisitorRepository;
import org.springframework.stereotype.Service;

@Service
public class VisitorServiceImpl implements VisitorService {

    private final VisitorRepository visitorRepository;

    public VisitorServiceImpl(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public Visitor save(Visitor visitor){
        return visitorRepository.save(visitor);
    }
}
