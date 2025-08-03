package com.tnguyenvna.BookManagementSystem.BookManagementSystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.request.BookRequest;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response.ApiResponse;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response.BookResponse;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response.PaginatedBookResponse;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.service.BookServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.tnguyenvna.BookManagementSystem.BookManagementSystem.util.ApiResponseUtils.buildSuccessResponse;

import java.time.Year;
import java.util.List;

@Tag(name = "Mobilise Book Management System - BookLibrary Controller", description = "Exposes REST APIs for Book Library Controller")
@RestController
@RequestMapping("/book")
@Slf4j
@RequiredArgsConstructor
public class BookController {
    private final BookServices bookServices;

    @Operation(summary = "Add New Book REST API", description = "This REST API is used to Add a New Book to a Database")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addNewBook(@RequestBody BookRequest bookRequest) {
        BookResponse response = bookServices.addNewBook(bookRequest);
        return ResponseEntity.status(HttpStatus.OK).body(buildSuccessResponse(response, HttpStatus.OK));
    }

    @Operation(summary = "Get ALL Books with Pagination REST API", description = "This REST API is used to Retrieve All Books with Pagination")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllBooksWithPagination(@RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        PaginatedBookResponse response = bookServices.getAllBooksWithPagination(pageNo, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(buildSuccessResponse(response, HttpStatus.OK));
    }

    @Operation(summary = "Get Book by ID REST API", description = "This REST API is used to Retrieve a Book By ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "HTTP Status 200 Ok")
    @GetMapping("/get/by/id/{id}")
    public ResponseEntity<ApiResponse> getBookById(@PathVariable Long id) {
        BookResponse response = bookServices.getBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(buildSuccessResponse(response, HttpStatus.OK));
    }

    @Operation(summary = "Search Books By Title or Author or ISBN REST API", description = "This REST API is used to Search Books by Title, Author or ISBN")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @GetMapping("/search/by/searchText/{searchText}")
    public ResponseEntity<ApiResponse> searchBookByTitleOrAuthorOrIsbn(@PathVariable String searchText) {
        List<BookResponse> bookResponses = bookServices.searchBookTitleOrAuthorOrIsbn(searchText);
        return ResponseEntity.status(HttpStatus.OK).body(buildSuccessResponse(bookResponses, HttpStatus.OK));
    }

    @Operation(summary = "Search Books by Publication Year REST API", description = "This REST API is used to Search Books by Publication Year")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @GetMapping("search/by/publicationYear/{publicationYear}")
    public ResponseEntity<ApiResponse> searchBookByPublicationYear(@PathVariable Year publicationYear) {
        List<BookResponse> response = bookServices.searchBookByPublicationYear(publicationYear);
        return ResponseEntity.status(HttpStatus.OK).body(buildSuccessResponse(response, HttpStatus.OK));
    }

    @Operation(summary = "Search Books by Publication Year REST API", description = "This REST API is used to Search  Books by Publication Year")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @PutMapping("/update/by/id/{id}")
    public ResponseEntity<ApiResponse> updateBookDetails(@PathVariable Long id,
            @Valid @RequestBody BookRequest updateRequest) {
        BookResponse response = bookServices.updateBook(id, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(buildSuccessResponse(response, HttpStatus.OK));
    }

    @Operation(summary = "Delete Book By ID REST API", description = "This REST API is used to Delete a book by ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @DeleteMapping("/delete/by/id/{id}")
    public ResponseEntity<ApiResponse> deleteBookById(@PathVariable Long id) {
        ApiResponse response = bookServices.deleteBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(buildSuccessResponse(response, HttpStatus.OK));
    }
}
