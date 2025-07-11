package com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.request;

import java.time.Year;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookRequest {
    private String title;
    private String author;
    private String isbn;
    private Integer quantity;
    private Year publivationYear;

}
