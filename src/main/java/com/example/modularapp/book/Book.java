package com.example.modularapp.book;

public class Book {
    public Long id;
    public String title;
    public String author;

    public Book() {}

    public Book(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
}
