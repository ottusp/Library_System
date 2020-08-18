package com.ottofernan.librarycrud.services.visitor;

import com.ottofernan.librarycrud.domain.dtos.BookDTO;
import com.ottofernan.librarycrud.domain.dtos.VisitorDTO;
import com.ottofernan.librarycrud.domain.enums.Status;
import com.ottofernan.librarycrud.domain.models.Book;
import com.ottofernan.librarycrud.domain.models.Visitor;
import com.ottofernan.librarycrud.domain.models.VisitorBook;
import com.ottofernan.librarycrud.repositories.VisitorRepository;
import com.ottofernan.librarycrud.services.restbook.RestBookService;
import org.springframework.stereotype.Service;

@Service
public class VisitorServiceImpl implements VisitorService {

    private final VisitorRepository visitorRepository;
    private final RestBookService restBookService;

    public VisitorServiceImpl(VisitorRepository visitorRepository, RestBookService restBookService) {
        this.visitorRepository = visitorRepository;
        this.restBookService = restBookService;
    }

    public VisitorDTO save(VisitorDTO visitor){
        Visitor savedVisitor = visitorRepository.save(VisitorDTO.toModel(visitor));
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

    public Status auth (VisitorBook tested){
        try {
            VisitorDTO correctVisitor = findByFirstName(tested.getVisitor().getFirstName());

            if (correctVisitor == null) return Status.NOT_FOUND;

            if (Visitor.isPasswordCorrect(tested.getVisitor(), correctVisitor)) return Status.SUCCESS;
            else return Status.FAIL;

        } catch (NullPointerException npe) {
            return Status.NOT_FOUND;
        }
    }

    public Status rent (VisitorBook visitorBook) {
        BookDTO bookDTO = restBookService.findById(visitorBook.getBook().getId());
        VisitorDTO visitorDTO = findByFirstName(visitorBook.getVisitor().getFirstName());
        Book book = BookDTO.toModel(bookDTO);

        if(book.rent(visitorDTO)) {
            save(visitorDTO);
            restBookService.update(book.toDto());

            return Status.SUCCESS;
        }
        else return Status.FAIL;
    }

    public Status returnBook(VisitorBook visitorBook) {
        VisitorDTO checkedVisitor = findById(visitorBook.getVisitor().getId());
        if(checkedVisitor == null) return Status.NOT_FOUND;

        BookDTO checkedBookDTO = restBookService.findById(visitorBook.getBook().getId());
        Book checkedBook = BookDTO.toModel(checkedBookDTO);

        if(!checkedBook.returnBook(checkedVisitor)) return Status.FAIL;

        save(checkedVisitor);
        restBookService.update(checkedBook.toDto());
        return Status.SUCCESS;
    }
}
