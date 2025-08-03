package com.tnguyenvna.BookManagementSystem.BookManagementSystem.service.mapper;

import org.springframework.stereotype.Component;

import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.request.BookRequest;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response.BookResponse;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.entity.BookLibrary;


@Component
public class BookMapper {

    public BookLibrary mapBookRequestToBookLibrary(BookRequest bookRequest) {
        BookLibrary bookLibrary = new BookLibrary();
        bookLibrary.setTitle(bookRequest.getTitle());
        bookLibrary.setAuthor(bookRequest.getAuthor());
        bookLibrary.setIsbn(bookRequest.getIsbn());
        bookLibrary.setQuantity(bookRequest.getQuantity());
        bookLibrary.setPublicationYear(bookRequest.getPublicationYear());
        return bookLibrary;
    }

    public BookResponse mapBookLibraryToBookResponse(BookLibrary bookLibrary) {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(bookLibrary.getId());
        bookResponse.setTitle(bookLibrary.getTitle());
        bookResponse.setAuthor(bookLibrary.getAuthor());
        bookResponse.setIsbn(bookLibrary.getIsbn());
        bookResponse.setQuantity(bookLibrary.getQuantity());
        bookResponse.setPublicationYear(bookLibrary.getPublicationYear());
        return bookResponse;

    }
}
