package my.libraryservice.dao;

import my.libraryservice.models.Book;
import my.libraryservice.models.Person;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query(
                "SELECT * FROM person",
                new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        return jdbcTemplate.query(
                        "SELECT * FROM person WHERE id=?",
                        new BeanPropertyRowMapper<>(Person.class),
                        id)
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update(
                "INSERT INTO person(full_name, year_of_birth) VALUES(?, ?)",
                person.getFullName(),
                person.getYearOfBirth());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update(
                "UPDATE person SET full_name=?, year_of_birth=? WHERE id=?",
                updatedPerson.getFullName(),
                updatedPerson.getYearOfBirth(),
                id);
    }

    public void delete(int id) {
        jdbcTemplate.update(
                "DELETE FROM person WHERE id=?",
                id);
    }

    public List<Book> getBooksByPersonId(int id) {
        return jdbcTemplate.query(
                "SELECT * FROM book WHERE person_id = ?",
                new BeanPropertyRowMapper<>(Book.class),
                id);
    }

    // Для валидации уникальности ФИО
    public Optional<Person> getPersonByFullName(String fullName) {
        return jdbcTemplate.query(
                        "SELECT * FROM person WHERE full_name=?",
                        new BeanPropertyRowMapper<>(Person.class),
                        fullName)
                .stream().findAny();
    }
}