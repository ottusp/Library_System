package com.ottofernan.librarycrud.bootstrap;

import com.ottofernan.librarycrud.models.Author;
import com.ottofernan.librarycrud.models.Book;
import com.ottofernan.librarycrud.models.Visitor;
import com.ottofernan.librarycrud.repositories.AuthorRepository;
import com.ottofernan.librarycrud.repositories.BookRepository;
import com.ottofernan.librarycrud.repositories.VisitorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final VisitorRepository visitorRepository;

    public DataLoader(AuthorRepository authorRepository, BookRepository bookRepository,
                      VisitorRepository visitorRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.visitorRepository = visitorRepository;
    }

    @Override
    public void run(String... args) {
        authorRepository.findAll().forEach(System.out::println);
        bookRepository.findAll().forEach(System.out::println);

        Book einsteinBio = bookRepository.findByTitle("Einstein Biography");
        Book leonardoBio = bookRepository.findByTitle("Leonardo da Vinci Biography");
        Book steveBio = bookRepository.findByTitle("Steve Jobs");
        Author walter = authorRepository.findByFirstName("Walter");

        Book theFault = bookRepository.findByTitle("The fault in our stars");
        Book letIt = bookRepository.findByTitle("Let it snow");
        Author john = authorRepository.findByFirstName("John");
        Author lauren = authorRepository.findByFirstName("Lauren");

        Author peter = new Author();
        peter.setFirstName("Peter");
        peter.setLastName("Gray");
        authorRepository.save(peter);
        Book freeTo = bookRepository.findByTitle("Free to learn");


        einsteinBio.setAuthors(Set.of(walter));
        leonardoBio.setAuthors(Set.of(walter));
        steveBio.setAuthors(Set.of(walter));
        freeTo.setAuthors(Set.of(peter));
        theFault.setAuthors(Set.of(john));
        letIt.setAuthors(Set.of(john, lauren));

        Visitor otto = new Visitor();
        otto.setFirstName("Otto");
        otto.setLastName("Fernandes");
        otto.setPassword("123456");
        otto.setBooks(Set.of(einsteinBio, freeTo));
        visitorRepository.save(otto);

        einsteinBio.setVisitors(Set.of(otto));
        freeTo.setVisitors(Set.of(otto));

        bookRepository.save(letIt);
        bookRepository.save(theFault);
        bookRepository.save(freeTo);
        bookRepository.save(steveBio);
        bookRepository.save(leonardoBio);
        bookRepository.save(einsteinBio);
    }
}
