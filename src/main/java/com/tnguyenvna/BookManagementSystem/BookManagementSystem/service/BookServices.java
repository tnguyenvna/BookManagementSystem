package com.tnguyenvna.BookManagementSystem.BookManagementSystem.service;

import java.time.Year;
import java.util.List;

import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.request.BookRequest;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response.ApiResponse;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response.BookResponse;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response.PaginatedBookResponse;

public interface BookServices {

    BookResponse addNewBook(BookRequest bookRequest);

    PaginatedBookResponse getAllBooksWithPagination(int pageNo, int pageSize);

    BookResponse getBookById(Long id);

    List<BookResponse> searchBookTitleOrAuthorOrIsbn(String searchText);

    List<BookResponse> searchBookByPublicationYear(Year publicationYear);

    BookResponse updateBook(Long id, BookRequest bookRequest);

    ApiResponse daleteBookById(Long id);

}
