DROP TABLE Book;
DROP TABLE Person;

CREATE TABLE IF NOT EXISTS Person
(
    id            int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    full_name     varchar(100) NOT NULL UNIQUE,
    year_of_birth int          NOT NULL
);

CREATE TABLE IF NOT EXISTS Book
(
    id        int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title     varchar(100) NOT NULL,
    author    varchar(100) NOT NULL,
    year      int          NOT NULL,
    person_id int          REFERENCES Person (id) ON DELETE SET NULL
);