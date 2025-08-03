package com.tnguyenvna.BookManagementSystem.BookManagementSystem;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Year;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.request.BookRequest;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response.ApiResponse;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response.BookResponse;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response.PaginatedBookResponse;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.entity.BookLibrary;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.NotFoundException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.exception.ValidTitleException;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.repository.BookRepository;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.service.impl.BookServiceImpl;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.service.mapper.BookMapper;
import com.tnguyenvna.BookManagementSystem.BookManagementSystem.validator.BookInfoValidations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class BookManagementSystemApplicationTests {
	@Mock
	private BookRepository bookRepository;
	@InjectMocks
	private BookServiceImpl bookServiceImpl;
	@Mock
	private BookInfoValidations bookInfoValidations;
	@Mock
	private BookMapper bookMapper;
	@Mock
	private ModelMapper modelMapper;

	@Test
	public void testAddNewBook() throws ValidTitleException {

		// Set up test data
		BookRequest bookRequest = new BookRequest("Samle Title", "Sample Author", "123456789", 5, Year.of(2022));
		BookLibrary bookLibrary = new BookLibrary(1L, "Sample Title", "Sample Author", "123456789", 5, Year.of(2022));

		// Mock behavior
		given(bookMapper.mapBookRequestToBookLibrary(bookRequest)).willReturn(bookLibrary);
		given(bookRepository.save(bookLibrary)).willReturn(bookLibrary);
		given(bookMapper.mapBookLibraryToBookResponse(bookLibrary))
				.willReturn(new BookResponse(1L, "Sample Title", "Sample Author", "123456789", 5, Year.of(2022)));

		// Call the method
		BookResponse bookResponse = bookServiceImpl.addNewBook(bookRequest);

		verify(bookInfoValidations).requiredTitleField(bookRequest.getTitle());
		verify(bookInfoValidations).requiredAuthorField(bookRequest.getAuthor());
		verify(bookInfoValidations).isBookTitleAlreadyExists(bookRequest.getTitle());
		verify(bookInfoValidations).validateTitle(bookRequest.getTitle());
		verify(bookInfoValidations).validateAuthor(bookRequest.getAuthor());
		verify(bookInfoValidations).validatePublicationYear(bookRequest.getPublicationYear());
		verify(bookRepository).save(bookLibrary);
		verify(bookMapper).mapBookRequestToBookLibrary(bookRequest);
		verify(bookMapper).mapBookLibraryToBookResponse(bookLibrary);
		verify(bookMapper, times(1)).mapBookRequestToBookLibrary(bookRequest);
		verify(bookRepository, times(1)).save(bookLibrary);

		assertNotNull(bookResponse);
		assertEquals("Sample Title", bookResponse.getTitle());
		assertEquals("Sample Author", bookResponse.getAuthor());
		assertEquals("123456789", bookResponse.getIsbn());
		assertEquals(5, bookResponse.getQuantity());
		assertEquals(Year.of(2022), bookResponse.getPublicationYear());
	}

	@Test
	public void testGetAllBooksWithPagination() {

		int pageNo = 1;
		int pageSize = 10;
		Page<BookLibrary> mockPage = Mockito.mock(Page.class);

		given(bookRepository.findAll(PageRequest.of(pageNo, pageSize))).willReturn(mockPage);
		given(mockPage.getNumberOfElements()).willReturn(0);
		given(mockPage.getSize()).willReturn(pageSize);
		given(mockPage.stream()).willReturn(Stream.empty());

		PaginatedBookResponse response = bookServiceImpl.getAllBooksWithPagination(pageNo, pageSize);

		verify(bookRepository).findAll(PageRequest.of(pageNo, pageSize));

		assertNotNull(response);
		assertEquals(Collections.emptyList(), response.getContents());
		assertEquals(pageSize, response.getPageSize());
	}

	@Test
	public void testGetBookById() {
		Long bookId = 1L;
		BookLibrary bookLibrary = new BookLibrary(1L, "Sample Title", "Sample Author", "123456789", 5, Year.of(2022));

		given(bookRepository.findById(bookId)).willReturn(Optional.of(bookLibrary));
		given(bookMapper.mapBookLibraryToBookResponse(bookLibrary))
				.willReturn(new BookResponse(1L, "Sample Title", "Sample Author", "123456789", 5, Year.of(2022)));

		BookResponse bookResponse = bookServiceImpl.getBookById(bookId);

		verify(bookRepository).findById(bookId);
		verify(bookMapper).mapBookLibraryToBookResponse(bookLibrary);

		assertNotNull(bookResponse);
		assertEquals(1L, bookResponse.getId());
		assertEquals("Sample Title", bookResponse.getTitle());
		assertEquals("Sample Author", bookResponse.getAuthor());
		assertEquals("123456789", bookResponse.getIsbn());
		assertEquals(5, bookResponse.getQuantity());
		assertEquals(Year.of(2022), bookResponse.getPublicationYear());
	}

	@Test
	public void testGetBookById_WhenBookNotFound() {
		// Set up test data
		Long bookId = 1L;

		// Mock behavior for book not found
		given(bookRepository.findById(bookId)).willReturn(Optional.empty());

		// Call the method and expect an exception
		assertThrows(NotFoundException.class, () -> bookServiceImpl.getBookById(bookId));

		// Verify interactions
		verify(bookRepository).findById(bookId);
	}

	@Test
	public void testSearchBookByTitleOrAuthorOrIsbn() {

		String searchText = "Sample";
		BookLibrary bookLibrary = new BookLibrary(1L, "Sample Title", "Sample Author", "123456789", 5, Year.of(2022));
		List<BookLibrary> bookList = Collections.singletonList(bookLibrary);

		given(bookRepository.searchByTitleOrAuthorOrIsbn(searchText, searchText, searchText)).willReturn(bookList);
		given(modelMapper.map(bookLibrary, BookResponse.class))
				.willReturn(new BookResponse(1L, "Sample Title", "Sample Author", "123456789", 5, Year.of(2022)));

		List<BookResponse> bookResponses = bookServiceImpl.searchBookTitleOrAuthorOrIsbn(searchText);

		verify(bookRepository).searchByTitleOrAuthorOrIsbn(searchText, searchText, searchText);
		verify(modelMapper).map(bookLibrary, BookResponse.class);

		assertNotNull(bookResponses);
		assertEquals(1, bookResponses.size());
		BookResponse response = bookResponses.get(0);
		assertEquals(1L, response.getId());
		assertEquals("Sample Title", response.getTitle());
		assertEquals("Sample Author", response.getAuthor());
		assertEquals("123456789", response.getIsbn());
		assertEquals(5, response.getQuantity());
		assertEquals(Year.of(2022), response.getPublicationYear());
	}

	@Test
	public void testSearchBookbyPublicationYear() {

		Year publicationYear = Year.of(2022);
		BookLibrary bookLibrary = new BookLibrary(1L, "Sample Title", "Sample Author", "123456789", 5, publicationYear);
		List<BookLibrary> bookList = Collections.singletonList(bookLibrary);

		given(bookRepository.findAllBooksByPublicationYear(publicationYear)).willReturn(bookList);
		given(modelMapper.map(bookLibrary, BookResponse.class))
				.willReturn(new BookResponse(1L, "Sample Title", "Sample Author", "123456789", 5, publicationYear));

		List<BookResponse> responses = bookServiceImpl.searchBookByPublicationYear(publicationYear);

		verify(bookRepository).findAllBooksByPublicationYear(publicationYear);
		verify(modelMapper).map(bookLibrary, BookResponse.class);

		assertNotNull(responses);
		assertEquals(1, responses.size());
		BookResponse response = responses.get(0);
		assertEquals(1L, response.getId());
		assertEquals("Sample Title", response.getTitle());
		assertEquals("Sample Author", response.getAuthor());
		assertEquals("123456789", response.getIsbn());
		assertEquals(5, response.getQuantity());
		assertEquals(publicationYear, response.getPublicationYear());
	}

	@Test
	public void testUpdateBook() {
		Long bookId = 1L;
		BookRequest bookRequest = new BookRequest("Updated Title", "Updated Author", "987654321", 3, Year.of(2023));

		given(bookRepository.findById(bookId)).willReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> bookServiceImpl.updateBook(bookId, bookRequest));
	}

	@Test
	public void testDeleteBookId_Success() {
		Long bookId = 1L;

		ApiResponse response = bookServiceImpl.deleteBookById(bookId);

		verify(bookRepository).deleteById(bookId);

		assertNotNull(response);
		assertNull(response.getMessage());
		assertEquals(HttpStatus.OK, response.getStatus());
		assertNotNull(response.getDateTime());
		assertNull(response.getData());
	}

	@Test
	public void testDeleteBookById_Exception() {
		Long bookId = 1L;

		String errorMessage = "Error occurred while deleting the book";

		doThrow(new RuntimeException(errorMessage)).when(bookRepository).deleteById(bookId);

		Exception exception = assertThrows(NotFoundException.class, () -> bookServiceImpl.deleteBookById(bookId));

		verify(bookRepository).deleteById(bookId);

		// assertEquals("Error Occurred while deleting the Book: ", errorMessage, exception.getMessage());
		assertEquals(errorMessage, exception.getMessage());

	}
}