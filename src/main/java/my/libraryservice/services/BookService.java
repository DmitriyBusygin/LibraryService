package my.libraryservice.services;

import my.libraryservice.models.Book;
import my.libraryservice.models.Person;
import my.libraryservice.repository.BookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Book> findWithPagination(Integer page, Integer booksPerPage) {
        Pageable pageable = PageRequest.of(page, booksPerPage);
        return bookRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public Book findById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void save(Book book) {
        bookRepository.save(book);
    }

    public void update(int id, Book book) {
        book.setId(id);
        bookRepository.save(book);
    }

    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    // Освобождает книгу (этот метод вызывается, когда человек возвращает книгу в библиотеку)
    public void release(int bookId) {
        bookRepository.findById(bookId).ifPresent(
                book -> book.setPersonId(null)
        );
    }

    // Назначает книгу человеку (этот метод вызывается, когда человек забирает книгу из библиотеки)
    public void assign(int bookId, Person selectedPerson) {
        bookRepository.findById(bookId).ifPresent(
                book -> book.setPersonId(selectedPerson)
        );
    }

    // Возвращает null если книга ни за кем ни числится
    @Transactional(readOnly = true)
    public Optional<Person> getBookOwner(int bookId) {
        return bookRepository.findById(bookId).map(Book::getPersonId);
    }
}
