package com.example.modularapp.book;

import com.example.modularapp.author.Author;
import com.example.modularapp.author.AuthorRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class BookServiceTest {
    @Inject
    BookService bookService;

    @Inject
    AuthorRepository authorRepository;



    @BeforeEach
    void setup() {

    }

    @Test
    @Transactional
    void shouldAddBook() {
        Author author = new Author();
        author.name = "John Doe";
        authorRepository.persist(author);

        BookDTO dto = new BookDTO();
        dto.title = "Clean Code";
        dto.year = 2025;
        dto.authorId = author.id;

        Book book = bookService.add(dto);

        assertNotNull(book.id);
        assertEquals("Clean Code", book.title);
        assertEquals(author.id, book.author.id );
    }
    @Test
    @Transactional
    public void shouldReturnListOfBooks() {
        Author author = new Author();
        author.name = "Jane Smith";
        authorRepository.persist(author);

        BookDTO dto1 = new BookDTO();
        dto1.title = "Book One";
        dto1.year = 2021;
        dto1.authorId = author.id;
        bookService.add(dto1);

        BookDTO dto2 = new BookDTO();
        dto2.title = "Book Two";
        dto2.year = 2023;
        dto2.authorId = author.id;
        bookService.add(dto2);

        List<Book> books = bookService.listAll();
        assertTrue(books.size() >= 2);
        assertTrue(books.stream().anyMatch(b -> b.title.equals("Book One")));
        assertTrue(books.stream().anyMatch(b -> b.title.equals("Book Two")));
    }

    @Test
    @Transactional
    public void shouldUpdateBook() {
        Author author = new Author();
        author.name = "Initial Author";
        authorRepository.persist(author);

        BookDTO createDto = new BookDTO();
        createDto.title = "Original Title";
        createDto.year = 2020;
        createDto.authorId = author.id;
        Book book = bookService.add(createDto);

        Author newAuthor = new Author();
        newAuthor.name = "Updated Author";
        authorRepository.persist(newAuthor);

        BookDTO updateDto = new BookDTO();
        updateDto.title = "Updated Title";
        updateDto.year = 2024;
        updateDto.authorId = newAuthor.id;

        Book updatedBook = bookService.update(book.id, updateDto);
        assertEquals("Updated Title", updatedBook.title);
        assertEquals(2024, updatedBook.year);
        assertEquals(newAuthor.id, updatedBook.author.id);
    }

    @Test
    @Transactional
    void shouldDeleteBook() {
        Author author = new Author();
        author.name = "Initial Author";
        authorRepository.persist(author);

        Book book = bookService.add(new BookDTO() {{
            title = "To Delete";
            year = 2025;
            authorId = author.id;
        }});

        boolean deleted = bookService.delete(book.id);

        assertTrue(deleted);
        assertNull(bookService.findById(book.id));
    }
}
