package com.tnguyenvna.BookManagementSystem.BookManagementSystem.service.impl;

import java.time.Year;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.request.BookRequest;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response.ApiResponse;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response.BookResponse;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response.PaginatedBookResponse;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.entity.BookLibrary;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.AlreadyExistsException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.AuthorRequiredException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.BookCreationException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.NotFoundException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.TitleRequiredException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.ValidAuthorException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.ValidPublicationYearException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.ValidTitleException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.repository.BookRepository;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.service.BookServices;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.service.mapper.BookMapper;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.validator.BookInfoValidations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookServices {
    private final BookRepository bookRepository;
    private final BookInfoValidations bookInfoValidations;
    private final BookMapper bookMapper;
    private final ModelMapper modelMapper;

    @Override
    public BookResponse addNewBook(BookRequest bookRequest) {
        try {
            bookInfoValidations.requiredTitleField(bookRequest.getTitle());
            bookInfoValidations.requiredAuthorField(bookRequest.getAuthor());
            bookInfoValidations.isBookTitleAlreadyExists(bookRequest.getTitle());
            bookInfoValidations.validateTitle(bookRequest.getTitle());
            bookInfoValidations.validateAuthor(bookRequest.getAuthor());
            bookInfoValidations.validatePublicationYear(bookRequest.getPublivationYear());

            BookLibrary bookLibrary = bookMapper.mapBookRequestToBookLibrary(bookRequest);
            bookRepository.save(bookLibrary);

            BookResponse bookResponse = bookMapper.mapBookLibraryToBookResponse(bookLibrary);
            log.info("new Book saved successfully");
            return bookResponse;
        } catch (AlreadyExistsException | ValidTitleException | ValidPublicationYearException | ValidAuthorException
                | TitleRequiredException | AuthorRequiredException e) {
            throw new BookCreationException("Error Occurred while Adding New Book: " + e.getMessage());
        }
    }

    @Override
    public PaginatedBookResponse getAllBooksWithPagination(int pageNo, int pageSize) {
        try {
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            Page<BookLibrary> booklist = bookRepository.findAll(pageable);
            log.info("Book List successfully retrieved with Pagination");
            List<BookResponse> collect = booklist.stream()
                    .map(bookMapper::mapBookLibraryToBookResponse)
                    .collect(Collectors.toList());
            return PaginatedBookResponse.builder()
                    .contents(collect)
                    .pageElementCount(booklist.getNumberOfElements())
                    .pageSize(booklist.getSize())
                    .build();
        } catch (Exception e) {
            log.info("Error occurred while retrieving all Books with pagination: " + e.getMessage());
            throw new NotFoundException("Error Occurred while retrieving Book List: " + e.getMessage());
        }
    }

    @Override
    public BookResponse getBookById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBookById'");
    }

    @Override
    public List<BookResponse> searchBookTitleOrAuthorOrIsbn(String searchText) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchBookTitleOrAuthorOrIsbn'");
    }

    @Override
    public List<BookResponse> searchBookByPublicationYear(Year publicationYear) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchBookByPublicationYear'");
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest bookRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBook'");
    }

    @Override
    public ApiResponse daleteBookById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'daleteBookById'");
    }

}
