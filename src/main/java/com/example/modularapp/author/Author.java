package com.example.modularapp.author;

import com.example.modularapp.book.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Author extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String name;

    @OneToMany(mappedBy = "author")
    @JsonIgnore // menghindari circular
    public List<Book> books;
}
