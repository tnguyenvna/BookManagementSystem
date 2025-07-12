package com.tnguyenvna.BookManagementSystem.BookManagementSystem.repository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tnguyenvna.BookManagementSystem.BookManagementSystem.entity.BookLibrary;

public interface BookRepository extends JpaRepository<BookLibrary, Long> {

    Optional<BookLibrary> findBookByTitleIgnoreCase(String bookTitle);

    List<BookLibrary> findAllBooksByPublicationYear(Year publicationYear);

    @Query("SELECT ic FROM BookLibrary ic WHERE LOWER(ic.title) LIKE CONCAT('%', COALESCE(LOWER(:title), ''), '%') " +
            "OR LOWER(ic.author) LIKE CONCAT('%', COALESCE(LOWER(:author), ''), '%') " +
            "OR LOWER(ic.isbn) LIKE CONCAT('%', COALESCE(LOWER(:isbn), ''), '%')")
    List<BookLibrary> searchByTitleOrAuthorOrIsbn(@Param("title") String title, @Param("author") String author,
            @Param("isbn") String isbn);
}
