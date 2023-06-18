package scopeland.libraryapp.apiController;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import scopeland.libraryapp.LibraryappApplication;
import scopeland.libraryapp.entities.Book;
import scopeland.libraryapp.enums.BookStatus;
import scopeland.libraryapp.exceptions.BookAlreadyBorrowedException;
import scopeland.libraryapp.exceptions.BookAlreadyExistsException;
import scopeland.libraryapp.exceptions.BookNotFoundException;
import scopeland.libraryapp.exceptions.SearchByYearsEndBeforeStartException;

/*
 * A collection of integration tests for the booking controller
 * Initial test data generated from the DatabaseLoader
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = LibraryappApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookControllerTests {

	@Autowired
	private MockMvc mvc;

	// region List Books
	/*
	 * Test the list books command
	 * - First item in json has a valid value
	 * - Expect the 3 items we added to be in the json (no more or no less)
	 */
	@Test
	public void givenBooks_whenListBook_thenStatus200()
			throws Exception {

		// Arrange - Already handled by Database Loader

		// Act
		var results = mvc.perform(get("/api/books")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		results.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].isbn").value("9781473619791"))
				.andExpect(jsonPath("$.length()").value(3));
	}
	// endregion

	// region Get By ISBN

	/*
	 * Test the get books command
	 * If book exists, return 200
	 */
	@Test
	public void givenBookExists_whenGetBookByIsbn_thenReturnBookWithStatus200()
			throws Exception {

		// Arrange - Already handled by Database Loader

		// Act
		var results = mvc.perform(get("/api/books/9781473619791")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		results.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.isbn").value("9781473619791"));
	}

	/*
	 * Test the get books command
	 * If book does exists, return BookNotFoundException
	 */
	@Test
	public void givenBookDoesNotExist_whenGetBookByIsbn_thenReturnError()
			throws Exception {

		// Arrange - Already handled by Database Loader

		// Act
		var results = mvc.perform(get("/api/books/9781473619123")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		results.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(
						result.getResolvedException() instanceof BookNotFoundException));
	}
	// endregion

	// region Add Book

	/*
	 * Test the add books command returns a 200 response if valud
	 */
	@Test
	public void givenValidBook_whenAddBook_thenStatus200()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("9781473619800", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 1979,
				BookStatus.AVAILABLE);

		// Act
		var addResults = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		// Assert
		addResults.andExpect(status().isOk());
	}

	/*
	 * Test the add books command adds a book, checked by seeing the list total
	 * increment to 4
	 * Separated this check out from the base test as it has a dependency on the
	 * list endpoint as well.
	 */
	@Test
	public void givenValidBook_whenAddBook_thenBookAddedToList()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("9781473614148", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 1979,
				BookStatus.AVAILABLE);

		// Act
		mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		var listResults = mvc.perform(get("/api/books")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		listResults.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(4));
	}

	/*
	 * Test the add books returns a bad request when the title is null
	 */
	@Test
	public void givenBookMissingTitle_whenAddBook_thenStatus400()
			throws Exception {

		// Arrange - Already handled by Database Loader
		Book newBook = new Book("9781473614133", null, "Douglas Adams", 1979,
				BookStatus.AVAILABLE);

		// Act
		var addResults = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		// Assert
		addResults.andExpect(status().isBadRequest());
	}

	/*
	 * Test the add books returns a bad request when the author is null
	 */
	@Test
	public void givenBookMissingAuthor_whenAddBook_thenStatus400()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("9781473614133", "The Hitchhiker's Guide to the Galaxy", null, 1979,
				BookStatus.AVAILABLE);

		// Act
		var addResults = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		// Assert
		addResults.andExpect(status().isBadRequest());
	}

	/*
	 * Test the add books returns a bad request when the publication year is null
	 */
	@Test
	public void givenBookMissingPublicationYear_whenAddBook_thenStatus400()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("9781473614133", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", null,
				BookStatus.AVAILABLE);

		// Act
		var addResults = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		// Assert
		addResults.andExpect(status().isBadRequest());
	}

	/*
	 * Test the add books returns a bad request when the book status is null
	 */
	@Test
	public void givenBookMissingBookStatus_whenAddBook_thenStatus400()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("9781473614133", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 1979,
				null);

		// Act
		var addResults = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		// Assert
		addResults.andExpect(status().isBadRequest());
	}

	/*
	 * Test the add books only returns success for isbns of length 10 and 13
	 */
	@Test
	public void givenBooksWithVaryingISBNLength_whenAddBook_thenStatus400()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book isbn9Book = new Book("123456789", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 1979,
				BookStatus.AVAILABLE);
		Book isbn10Book = new Book("1234567891", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 1979,
				BookStatus.AVAILABLE);
		Book isbn11Book = new Book("12345678912", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 1979,
				BookStatus.AVAILABLE);
		Book isbn12Book = new Book("123456789123", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 1979,
				BookStatus.AVAILABLE);
		Book isbn13Book = new Book("1234567891234", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 1979,
				BookStatus.AVAILABLE);
		Book isbn14Book = new Book("12345678912345", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 1979,
				BookStatus.AVAILABLE);

		// Act
		var addIsbn9Results = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(isbn9Book.mapToJson()));
		var addIsbn10Results = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(isbn10Book.mapToJson()));
		var addIsbn11Results = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(isbn11Book.mapToJson()));
		var addIsbn12Results = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(isbn12Book.mapToJson()));
		var addIsbn13Results = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(isbn13Book.mapToJson()));
		var addIsbn14Results = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(isbn14Book.mapToJson()));

		// Assert
		addIsbn9Results.andExpect(status().isBadRequest());
		addIsbn10Results.andExpect(status().isOk());
		addIsbn11Results.andExpect(status().isBadRequest());
		addIsbn12Results.andExpect(status().isBadRequest());
		addIsbn13Results.andExpect(status().isOk());
		addIsbn14Results.andExpect(status().isBadRequest());

	}

	/*
	 * Test the add books returns a 400 when as isbn has a letter in it
	 */
	@Test
	public void givenBooksWithLettersInIsbn_whenAddBook_thenStatus400()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("978147H614133", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 1979,
				BookStatus.AVAILABLE);

		// Act
		var addResults = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		// Assert
		addResults.andExpect(status().isBadRequest());
	}

	/*
	 * Test the add books returns a 200 when as isbn has dashes in it
	 */
	@Test
	public void givenBooksWitDashesInIsbn_whenAddBook_thenStatus200()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("978-1478614133", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 1979,
				BookStatus.AVAILABLE);

		// Act
		var addResults = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		// Assert
		addResults.andExpect(status().isOk());
	}

	/*
	 * Test the add books returns a 200 when as isbn has a space in it
	 */
	@Test
	public void givenBooksWithspacesInIsbn_whenAddBook_thenStatus200()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("978 1476141339", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 1979,
				BookStatus.AVAILABLE);

		// Act
		var addResults = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		// Assert
		addResults.andExpect(status().isOk());
	}

	/*
	 * Test the add books command does not add a book when it throws a 400
	 * Separated this check out from the base test as it has a dependency on the
	 * list endpoint as well.
	 */
	@Test
	public void givenInvalidBook_whenAddBook_thenBookNotAddedToList()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("9781473614141", null, "Douglas Adams", 1979,
				BookStatus.AVAILABLE);

		// Act
		mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		var listResults = mvc.perform(get("/api/books")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		listResults.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(3));
	}

	/*
	 * Test the add books command does not add or update a book if a book with the
	 * isbn being added already exists. Depends on get command, throws
	 * BookAlreadyExistsException
	 */
	@Test
	public void givenAlreadyExistingBook_whenAddBook_thenThrowsVookAlreadyExistsError()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("9781473619791", "The Long Way to a Small Angry Planet", "Becky Chambers", 2018,
				BookStatus.AVAILABLE);

		// Act
		var addResults = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		addResults.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(
						result.getResolvedException() instanceof BookAlreadyExistsException));
		// Assert

	}

	/*
	 * Test the add books command does not add or update a book if a book with the
	 * isbn being added already exists. Separated as Depends on get command
	 */
	@Test
	public void givenAlreadyExistingBook_whenAddBook_thenBookNotAddedToListAndExistingBookNotUpdated()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("9781473619791", "The Long Way to a Small Angry Planet", "Becky Chambers", 2018,
				BookStatus.AVAILABLE);

		// Act
		var addResults = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		mvc.perform(get("/api/books/9781473619791")
				.contentType(MediaType.APPLICATION_JSON));

		addResults.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(
						result.getResolvedException() instanceof BookAlreadyExistsException));
	}
	// endregion Add Book

	// region Search Books by Author

	/*
	 * When we search for an author with books
	 * An array with multiple entries is returned
	 */
	@Test
	public void givenBooks_whenSearchForValidAuthor_thenStatus200AndListReturned()
			throws Exception {

		// Arrange - Already handled by Database Loader

		// Act
		var results = mvc.perform(get("/api/books/search/author/Becky+Chambers")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		results.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].author").value("Becky Chambers"))
				.andExpect(jsonPath("$[1].author").value("Becky Chambers"))
				.andExpect(jsonPath("$.length()").value(2));
	}

	/*
	 * When we search for an author with no books
	 * An array with no entries is returned
	 */
	@Test
	public void givenBooks_whenSearchForAuthorReturnsNoResults_thenStatus200AndEmptyListReturned()
			throws Exception {

		// Arrange - Already handled by Database Loader

		// Act
		var results = mvc.perform(get("/api/books/search/author/joe")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		results.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(0));
	}
	// endregion

	// region Search By Publication Date

	/*
	 * When we search a year range with a book at its lower limit
	 * The book at the lower limit is returned in the array
	 */
	@Test
	public void givenBooks_whenSearchForValidPublicationYears_thenIncludeLowerLimit()
			throws Exception {

		// Arrange - Already handled by Database Loader

		// Act
		var results = mvc.perform(get("/api/books/search/publicationYear/range/2014/2017")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		results.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].publicationYear").value(2014))
				.andExpect(jsonPath("$.length()").value(1));
	}

	/*
	 * When we search a year range with a book at its upper limit
	 * The book at the upper limit is returned in the array
	 */
	@Test
	public void givenBooks_whenSearchForValidPublicationYears_thenIncludeUpperLimit()
			throws Exception {

		// Arrange - Already handled by Database Loader

		// Act
		var results = mvc.perform(get("/api/books/search/publicationYear/range/2015/2018")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		results.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].publicationYear").value(2018))
				.andExpect(jsonPath("$.length()").value(1));
	}

	/*
	 * When we search a year range with no book in its range
	 * we get an empty array
	 */
	@Test
	public void givenBooks_whenSearchForValidPublicationYears_ifNoneFoundReturnsEmptyList()
			throws Exception {

		// Arrange - Already handled by Database Loader

		// Act
		var results = mvc.perform(get("/api/books/search/publicationYear/range/2008/2010")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		results.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(0));
	}

	/*
	 * When we search a year range with books between its limits
	 * we return an array with multiple items
	 */
	@Test
	public void givenBooks_whenSearchForValidPublicationYears_thenIncludeInbetweenValues()
			throws Exception {

		// Arrange - Already handled by Database Loader

		// Act
		var results = mvc.perform(get("/api/books/search/publicationYear/range/2017/2020")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		results.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(2));
	}

	/*
	 * When we search with an end year before a start year
	 * we get a SearchByYearsEndBeforeStartException exception telling us this
	 */
	@Test
	public void givenBooks_whenSearchwithEndYearBeforeStartYear_thenThrowError()
			throws Exception {

		// Arrange - Already handled by Database Loader

		// Act
		var results = mvc.perform(get("/api/books/search/publicationYear/range/2019/2017")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		results.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(
						result.getResolvedException() instanceof SearchByYearsEndBeforeStartException));
	}

	/*
	 * When we search a year range with an invalid year in the url (ie. not a
	 * number)
	 * we return a bad request response
	 */
	@Test
	public void givenBooks_whenSearchForWithInvalidParams_thenThrowsError()
			throws Exception {

		// Arrange - Already handled by Database Loader

		// Act
		var results = mvc.perform(get("/api/books/search/publicationYear/range/2017/test")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		results.andExpect(status().isBadRequest());
	}

	// endregion

	// region Update Book
	/*
	 * Test the update books command updates a book, we return a 200.
	 */
	@Test
	public void givenValidBook_whenUpdateBook_thenStatus200()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("9781473619791", "The Long Way to a Small Angry Planet", "Becky Chambers", 2018,
				BookStatus.AVAILABLE);

		// Act
		var updateResults = mvc.perform(put("/api/books/9781473619791")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		// Assert
		updateResults.andExpect(status().isOk());
	}

	/*
	 * Test the update books command updates a book, checked by then getting updated
	 * book from db
	 * separated as depends on get book
	 */
	@Test
	public void givenValidBook_whenUpdateBook_thenBookUpdated()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("9781473619791", "The Long Way to a Small Angry Planet", "Becky Chambers", 2018,
				BookStatus.AVAILABLE);

		// Act
		mvc.perform(put("/api/books/9781473619791")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		var getResults = mvc.perform(get("/api/books/9781473619791")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		getResults.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.publicationYear").value(2018));
	}

	/*
	 * Test the update books returns a bad request when the title is null
	 */
	@Test
	public void givenBookMissingTitle_whenUpdateBook_thenStatus400()
			throws Exception {

		// Arrange - Already handled by Database Loader
		Book newBook = new Book("9781473619791", null, "Becky Chambers", 2018,
				BookStatus.AVAILABLE);

		// Act
		var updateResults = mvc.perform(put("/api/books/9781473619791")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		// Assert
		updateResults.andExpect(status().isBadRequest());
	}

	/*
	 * Test the update books returns a bad request when the author is null
	 */
	@Test
	public void givenBookMissingAuthor_whenUpdateBook_thenStatus400()
			throws Exception {

		// Arrange - Already handled by Database Loader
		Book newBook = new Book("9781473619791", "The Long Way to a Small Angry Planet", null, 2014,
				BookStatus.AVAILABLE);

		// Act
		var updateResults = mvc.perform(put("/api/books/9781473619791")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		// Assert
		updateResults.andExpect(status().isBadRequest());
	}

	/*
	 * Test the update books returns a bad request when the publication year is null
	 */
	@Test
	public void givenBookMissingPublicationYear_whenUpdateBook_thenStatus400()
			throws Exception {

		// Arrange - Already handled by Database Loader
		Book newBook = new Book("9781473619791", "The Long Way to a Small Angry Planet", "Becky Chambers", null,
				BookStatus.AVAILABLE);

		// Act
		var updateResults = mvc.perform(put("/api/books/9781473619791")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		// Assert
		updateResults.andExpect(status().isBadRequest());
	}

	/*
	 * Test the update books returns a bad request when the Book status is null
	 */
	@Test
	public void givenBookMissingBookStatus_whenUpdateBook_thenStatus400()
			throws Exception {

		// Arrange - Already handled by Database Loader
		Book newBook = new Book("9781473619791", "The Long Way to a Small Angry Planet", "Becky Chambers", 2014,
				null);

		// Act
		var updateResults = mvc.perform(put("/api/books/9781473619791")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		// Assert
		updateResults.andExpect(status().isBadRequest());
	}

	/*
	 * Test the update books command does not update or add a book if a book with
	 * the
	 * isbn being updateed already exists. Depends on get command so separated out
	 */
	@Test
	public void givenNotExistantISBN_whenUpdateBook_thenBookNotAddedToList()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("9781473614140", "The Hitchiker's Guide to the Galaxy", "Douglas Adams", 1979,
				BookStatus.AVAILABLE);

		// Act
		mvc.perform(put("/api/books/9781473614140")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		var getResults = mvc.perform(get("/api/books")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		getResults.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(3));
		;
	}

	/*
	 * Test the update books command does not update or add a book if a book with
	 * the
	 * isbn being updateed already exists. Depends on get command
	 */
	@Test
	public void givenNotExistantISBN_whenUpdateBook_thenBookAlreadyExistsExceptionThrown()
			throws Exception {

		// Arrange - Some handled by Database Loader
		Book newBook = new Book("9781473614140", "The Hitchiker's Guide to the Galaxy", "Douglas Adams", 1979,
				BookStatus.AVAILABLE);

		// Act
		var updateResults = mvc.perform(put("/api/books/9781473614140")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBook.mapToJson()));

		// Assert
		updateResults.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(
						result.getResolvedException() instanceof BookNotFoundException));
		;
	}
	// endregion

	// region Update Book Status

	/*
	 * Test that when we update a book status successfully, we get a 200
	 */
	@Test
	public void givenValidBook_whenUpdateBookStatus_thenStatus200()
			throws Exception {

		// Arrange - Some handled by Database Loader

		// Act
		var updateResults = mvc.perform(put("/api/books/9781473619791/updateStatus/BORROWED")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		updateResults.andExpect(status().isOk());
	}

	/*
	 * Test the update book status command updates a book, checked by then getting
	 * book from db
	 * Separated this check out from the base test as it has a dependency on the
	 * get endpoint as well.
	 */
	@Test
	public void givenValidBook_whenUpdateBookStatus_thenStatusIsUpdated()
			throws Exception {

		// Arrange - Some handled by Database Loader

		// Act
		mvc.perform(put("/api/books/9781473619791/updateStatus/BORROWED")
				.contentType(MediaType.APPLICATION_JSON));

		var listResults = mvc.perform(get("/api/books/9781473619791")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		listResults.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.bookStatus").value("BORROWED"));
	}

	/*
	 * Test the update books status command throws a BookNotFoundException if given
	 * an isbn that does not exist
	 */
	@Test
	public void givenInvalidBook_whenUpdateBookStatus_thenReturnError()
			throws Exception {

		// Arrange - Some handled by Database Loader

		// Act
		var updateResults = mvc.perform(put("/api/books/1111111111111/updateStatus/BORROWED")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		updateResults.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(
						result.getResolvedException() instanceof BookNotFoundException));
	}

	/*
	 * Test the update books status command throws a BookAlreadyBorrowedException if
	 * we try to borrow a book thats already borrowed
	 */
	@Test
	public void givenBookAlreadyBorrowed_whenTryBorrow_thenReturnError()
			throws Exception {

		// Arrange - Some handled by Database Loader

		// Act
		var updateResults = mvc.perform(put("/api/books/9780062877239/updateStatus/BORROWED")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		updateResults.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(
						result.getResolvedException() instanceof BookAlreadyBorrowedException));
	}
	// endregion

	// region Delete Book
	/*
	 * Test the delete books command returns a 200 if the book exists
	 */
	@Test
	public void givenExistingBooksISBN_whenDeleteBook_thenStatus200()
			throws Exception {

		// Arrange - Some handled by Database Loader

		// Act
		var deleteResults = mvc.perform(delete("/api/books/9781473619791")
				.contentType(MediaType.APPLICATION_JSON));
		// Assert
		deleteResults.andExpect(status().isOk());
	}

	/*
	 * Test the delete books command returns a BookNotFoundException if there is not
	 * a book with a matching isbn
	 */
	@Test
	public void givenNonExistantBooksISBN_whenDeleteBook_thenExceptionThrown()
			throws Exception {

		// Arrange - Some handled by Database Loade

		// Act
		var deleteResults = mvc.perform(delete("/api/books/9781473619000")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		deleteResults.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(
						result.getResolvedException() instanceof BookNotFoundException));
	}

	/*
	 * Test the delete books command returns a 200 if the book exists
	 * Test actual deletion by using list command and counting number of bookings.
	 */
	@Test
	public void givenExistingBooksISBN_whenDeleteBook_thenBookRemovedFromDB() throws Exception {

		// Act
		mvc.perform(delete("/api/books/9781473619791")
				.contentType(MediaType.APPLICATION_JSON));

		var listResults = mvc.perform(get("/api/books")
				.contentType(MediaType.APPLICATION_JSON));

		// Assert
		listResults.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(2));

	}
	// endregion
}
