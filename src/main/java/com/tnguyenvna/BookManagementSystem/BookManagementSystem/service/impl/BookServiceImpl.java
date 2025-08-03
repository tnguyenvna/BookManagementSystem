package com.tnguyenvna.BookManagementSystem.BookManagementSystem.service.impl;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
            bookInfoValidations.validatePublicationYear(bookRequest.getPublicationYear());

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
            log.error("Error occurred while retrieving all Books with pagination: " + e.getMessage());
            throw new NotFoundException("Error Occurred while retrieving Book List: " + e.getMessage());
        }
    }

    @Override
    public BookResponse getBookById(Long id) {
        try {
            BookLibrary bookLibrary = bookRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Book with id " + id + " does not exist"));
            return bookMapper.mapBookLibraryToBookResponse(bookLibrary);
        } catch (Exception ex) {
            log.error("Error while rretrieving Book: {}", ex.getMessage());
            throw new NotFoundException("Error Occurred while retrieving Book: " + ex.getMessage());
        }
    }

    @Override
    public List<BookResponse> searchBookTitleOrAuthorOrIsbn(String searchText) {
        try {
            List<BookLibrary> bookLibraries = bookRepository.searchByTitleOrAuthorOrIsbn(searchText, searchText,
                    searchText);

            List<BookResponse> bookResponses = new ArrayList<>();
            for (BookLibrary bookLibrary : bookLibraries) {
                BookResponse bookResponse = modelMapper.map(bookLibrary, BookResponse.class);
                bookResponses.add(bookResponse);
            }
            if (!bookResponses.isEmpty()) {
                log.info("Book successfuly retrieved from search text " + searchText);
                return bookResponses;
            } else {
                throw new NotFoundException("Book with search text " + searchText + " does not exist");
            }
        } catch (Exception ex) {
            log.error("Error while searching Boo: {}", ex.getMessage());
            throw new NotFoundException("Error Occurred while search Book: " + ex.getMessage());
        }
    }

    @Override
    public List<BookResponse> searchBookByPublicationYear(Year publicationYear) {
        try {
            List<BookLibrary> bookLibraries = bookRepository.findAllBooksByPublicationYear(publicationYear);
            List<BookResponse> bookResponses = new ArrayList<>();
            for (BookLibrary bookLibrary : bookLibraries) {
                BookResponse response = modelMapper.map(bookLibrary, BookResponse.class);
                bookResponses.add(response);
            }
            if (!bookResponses.isEmpty()) {
                log.info("Book successfuly retrieved by publication year: " + publicationYear);
                return bookResponses;
            } else {
                throw new NotFoundException("Book with publication year " + publicationYear + " Not Found");
            }
        } catch (Exception ex) {
            log.error("Error while searching Book: {}", ex.getMessage());
            throw new NotFoundException("Error Occurred while searching Book " + ex.getMessage());
        }
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest bookRequest) {
        try {
            Optional<BookLibrary> existingBook = bookRepository.findById(id);
            if (existingBook.isPresent()) {
                BookLibrary updateBook = existingBook.get();
                bookInfoValidations.isBookTitleAlreadyExists(bookRequest.getTitle());
                if (bookRequest.getTitle() != null && !bookRequest.getTitle().equals(updateBook.getTitle())) {
                    bookInfoValidations.validateTitle(bookRequest.getTitle());
                    updateBook.setTitle(bookRequest.getTitle());
                }
                if (bookRequest.getAuthor() != null && !bookRequest.getAuthor().equals(updateBook.getAuthor())) {
                    bookInfoValidations.validateAuthor(bookRequest.getAuthor());
                    updateBook.setAuthor(bookRequest.getAuthor());
                }
                if (bookRequest.getIsbn() != null) {
                    updateBook.setIsbn(bookRequest.getIsbn());
                }
                if (bookRequest.getPublicationYear() != null
                        && !bookRequest.getPublicationYear().equals(updateBook.getPublicationYear())) {
                    bookInfoValidations.validatePublicationYear(bookRequest.getPublicationYear());
                    updateBook.setPublicationYear(bookRequest.getPublicationYear());
                }

                BookLibrary bookLibrary = bookRepository.save(updateBook);
                BookResponse bookResponse = modelMapper.map(bookLibrary, BookResponse.class);
                log.info("Book successfully updated");
                return bookResponse;
            } else {
                throw new NotFoundException("Book with id " + id + " not found and cannot be updated");
            }
        } catch (Exception ex) {
            log.error("Error while updating Book: {}", ex.getMessage());
            throw new NotFoundException("Error Occurred while updating Book " + ex.getMessage());
        }

    }

    @Override
    public ApiResponse deleteBookById(Long id) {
        try {
            bookRepository.deleteById(id);
            log.info("Book successfully deleted with id: " + id);
        } catch (Exception ex) {
            log.error("Error while deleting Book: {}", ex.getMessage());
            // throw new NotFoundException("Error Occurred while deleting the Book: " + ex.getMessage());
            throw new NotFoundException("Error occurred while deleting the book");

        }
        return ApiResponse.builder().status(HttpStatus.OK).dateTime(LocalDateTime.now()).build();
    }

}
