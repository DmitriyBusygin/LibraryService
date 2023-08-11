package my.libraryservice.services;

import my.libraryservice.models.Book;
import my.libraryservice.models.Person;
import my.libraryservice.repository.PeopleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PeopleService {

    private final PeopleRepository peopleRepository;

    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Transactional(readOnly = true)
    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Person findById(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public void save(Person person) {
        peopleRepository.save(person);
    }

    public void update(int id, Person updadetPerson) {
        updadetPerson.setId(id);
        peopleRepository.save(updadetPerson);
    }

    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }
}
