package com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response;

import java.time.Year;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer quantity;
    private Year publicationYear;

}
