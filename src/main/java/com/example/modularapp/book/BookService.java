package com.example.modularapp.book;

import com.example.exception.NotFoundException;
import com.example.modularapp.author.Author;
import com.example.modularapp.author.AuthorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.*;

@ApplicationScoped
public class BookService {


    @Inject
    BookRepository repository;

    @Inject
    AuthorRepository authorRepository;

    public List<Book> listAll() {
        return repository.listAll();
    }

    @Transactional
    public Book add(BookDTO dto) {
        Author author = authorRepository.findById(dto.authorId);
        if (author == null) {
            throw new NotFoundException("Author not found");
        }
        Book book = new Book();
        book.title = dto.title;
        book.year = dto.year;
        book.author = author;
        repository.persist(book);
        return book;
    }

    public Book findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Book update(Long id, BookDTO dto) {
        Author author = authorRepository.findById(dto.authorId);
        if(author == null) {
            throw new NotFoundException("Author Not found");
        }

        Book book = repository.findById(id);
        if (book == null) return null;
        book.title = dto.title;
        book.author = author;
        return book;
    }

    @Transactional
    public boolean delete(Long id) {
        return repository.deleteById(id);
    }

}
