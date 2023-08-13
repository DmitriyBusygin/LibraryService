package my.libraryservice.services;

import my.libraryservice.models.Book;
import my.libraryservice.models.Person;
import my.libraryservice.repository.BooksRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(Sort.by("year"));
        } else {
            return booksRepository.findAll();
        }
    }

    @Transactional(readOnly = true)
    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        Pageable pageable = null;
        if (sortByYear) {
            pageable = PageRequest.of(page, booksPerPage, Sort.by("year"));
        } else {
            pageable = PageRequest.of(page, booksPerPage);
        }
        return booksRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public Book findById(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    public void save(Book book) {
        booksRepository.save(book);
    }

    public void update(int id, Book book) {
        book.setId(id);
        booksRepository.save(book);
    }

    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    // Освобождает книгу (этот метод вызывается, когда человек возвращает книгу в библиотеку)
    public void release(int bookId) {
        booksRepository.findById(bookId).ifPresent(
                book -> book.setPersonId(null)
        );
    }

    // Назначает книгу человеку (этот метод вызывается, когда человек забирает книгу из библиотеки)
    public void assign(int bookId, Person selectedPerson) {
        booksRepository.findById(bookId).ifPresent(
                book -> book.setPersonId(selectedPerson)
        );
    }

    // Возвращает null если книга ни за кем ни числится
    @Transactional(readOnly = true)
    public Optional<Person> getBookOwner(int bookId) {
        return booksRepository.findById(bookId).map(Book::getPersonId);
    }
}
