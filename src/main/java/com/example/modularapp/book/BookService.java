package com.example.modularapp.book;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class BookService {
    private final Map<Long, Book> books = new HashMap<>();
    private long currentId = 1;

    public List<Book> listAll() {
        return new ArrayList<>(books.values());
    }

    public Book add(BookDTO dto) {
        Book book = new Book(currentId++, dto.title, dto.author);
        books.put(book.id, book);
        return book;
    }

    public Book update(Long id, BookDTO dto) {
        Book book = books.get(id);
        if (book != null) {
            book.title = dto.title;
            book.author = dto.author;
        }
        return book;
    }

    public boolean delete(Long id) {
        return books.remove(id) != null;
    }

    public Book findById(Long id) {
        return books.get(id);
    }
}
