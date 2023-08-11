package my.libraryservice.dao;

import my.libraryservice.models.Book;
import my.libraryservice.models.Person;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query(
                "SELECT * FROM book",
                new BeanPropertyRowMapper<>(Book.class));
    }

    public Book show(int id) {
        return jdbcTemplate.query(
                        "SELECT * FROM book WHERE id=?",
                        new BeanPropertyRowMapper<>(Book.class),
                        id)
                .stream().findAny().orElse(null);
    }

    public void save(Book book) {
        jdbcTemplate.update(
                "INSERT INTO book(title, author, year) VALUES(?, ?, ?)",
                book.getTitle(),
                book.getAuthor(),
                book.getYear());
    }

    public void update(int id, Book updatedBook) {
        jdbcTemplate.update(
                "UPDATE book SET title=?, author=?, year=? WHERE id=?",
                updatedBook.getTitle(),
                updatedBook.getAuthor(),
                updatedBook.getYear(),
                id);
    }

    public void delete(int id) {
        jdbcTemplate.update(
                "DELETE FROM book WHERE id=?",
                id);
    }

    // Join'им таблицы Book и Person и получаем человека, которому принадлежит книга с указанным id
    public Optional<Person> getBookOwner(int id) {
        // Выбираем все колонки таблицы Person из объединенной таблицы
        return jdbcTemplate.query(
                        "SELECT person.* FROM book " +
                        "JOIN person ON person.id = book.person_id " +
                        "WHERE book.id = ?",
                        new BeanPropertyRowMapper<>(Person.class),
                        id)
                .stream().findAny();
    }

    // Освобождает книгу (этот метод вызывается, когда человек возвращает книгу в библиотеку)
    public void release(int id) {
        jdbcTemplate.update(
                "UPDATE Book SET person_id=NULL WHERE id=?",
                id);
    }

    // Назначает книгу человеку (этот метод вызывается, когда человек забирает книгу из библиотеки)
    public void assign(int id, Person selectedPerson) {
        jdbcTemplate.update(
                "UPDATE Book SET person_id=? WHERE id=?",
                selectedPerson.getId(),
                id);
    }
}
