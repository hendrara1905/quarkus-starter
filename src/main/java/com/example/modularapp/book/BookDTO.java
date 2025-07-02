package com.example.modularapp.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookDTO {

    @NotBlank
    public String title;

    @Min(1000)
    public int year;

    @NotNull
    public Long authorId;

}
