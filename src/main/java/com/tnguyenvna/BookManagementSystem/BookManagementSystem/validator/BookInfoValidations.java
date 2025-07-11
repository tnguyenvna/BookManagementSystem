package com.tnguyenvna.BookManagementSystem.BookManagementSystem.validator;

import java.time.Year;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.tnguyenvna.BookManagementSystem.BookManagementSystem.entity.BookLibrary;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.AlreadyExistsException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.AuthorRequiredException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.TitleRequiredException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.ValidAuthorException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.ValidPublicationYearException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.ValidTitleException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.repository.BookRepository;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookInfoValidations {

    private final BookRepository bookRepository;

    public void requiredTitleField(String title) throws ValidTitleException {
        if (title == null || title.isBlank() || title.isEmpty()) {
            log.error("Title field is required! and cannot be empty/blank");
            throw new TitleRequiredException("Title field is required! and cannot be empty/blank");
        }
    }

    public void requiredAuthorField(String author) throws ValidAuthorException {
        if (author == null || author.isBlank() || author.isEmpty()) {
            log.error("Author filed is required and cannot be empty/blank");
            throw new AuthorRequiredException("Author filed is required and cannot be empty/blank");
        }
    }

    public void isBookTitleAlreadyExists(String bookTitle) {
        Optional<BookLibrary> existingBookTitle = bookRepository.findBookByTitleIgnoreCase(bookTitle);
        if (existingBookTitle.isPresent()) {
            log.error("Book with this title: " + bookTitle + "already exists. Please use a different title");
            throw new AlreadyExistsException(
                    "Book with this title: " + bookTitle + "already exists. Please use a different title");
        }
    }

    public void validateTitle(String title) throws ValidTitleException {
        if (title == null || title.isBlank() || title.length() < 2 || title.length() > 50
                || !title.matches("[a-zA-Z0-9 ]+$")) {
            log.error("Title: " + title
                    + " must be between 2 and 50 characters and contain only English alphabet or a combination of English alphabet and number");
            throw new ValidTitleException(
                    "Title must be between 2 and 50 characters and contain only English alphabet or a combination of English alphabet and number");
        }
    }

    public void validateAuthor(String author) {
        if (author == null || author.isBlank() || author.length() < 2 || author.length() > 50) {
            log.error("Author Name(s): " + author + " must be between 2 and 50 characters");
            throw new ValidAuthorException("Author Name(s): \" +author +\" must be between 2 and 50 characters");
        }
    }

    public void validatePublicationYear(Year publicationYear) {
        if (publicationYear == null || publicationYear.isBefore(Year.of(2001)) || publicationYear.isAfter(Year.now())) {
            log.error(
                    "Publication year: " + publicationYear + " must be in the range between 2001 and the present year");
            throw new ValidPublicationYearException(
                    "Publication year must be in the range between 2001 and the present year");
        }
    }
}
