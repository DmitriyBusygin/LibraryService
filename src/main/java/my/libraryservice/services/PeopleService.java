package my.libraryservice.services;

import my.libraryservice.models.Book;
import my.libraryservice.models.Person;
import my.libraryservice.repository.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
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
    public List<Person> findWithPagination(Integer page, Integer peoplePerPage) {
        Pageable pageable = PageRequest.of(page, peoplePerPage);
        return peopleRepository.findAll(pageable).getContent();
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
            Hibernate.initialize(person.get().getBooks());
            // Мы внизу итерируемся по книгам, поэтому они точно будут загружены, но на всякий случай
            // не мешает всегда вызывать Hibernate.initialize()
            // (на случай, например, если код в дальнейшем поменяется и итерация по книгам удалится)

            // Проверка просроченности книг
            person.get().getBooks().forEach(book -> {
                long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                // 864000000 милисекунд = 10 суток
                if (diffInMillies > 864000000)
                    book.setExpired(true); // книга просрочена
            });
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }
}
