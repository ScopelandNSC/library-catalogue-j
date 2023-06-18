package scopeland.libraryapp.apiController;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import scopeland.libraryapp.entities.Book;
import scopeland.libraryapp.enums.BookStatus;
import scopeland.libraryapp.exceptions.BookNotFoundException;
import scopeland.libraryapp.service.BookService;
import scopeland.libraryapp.validation.books.constraint.IsbnConstraint;

/**
 * API Controller for interacting with Books Table
 **/
@RestController
@Validated
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    /*
     * Constructor with Spring Boot Dependency Injection to access repo
     */
    BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /*
     * Lists all stored books
     * 
     * @return - A list of all Books
     */
    @GetMapping
    List<Book> listBooks() {
        return bookService.listBooks();
    }

    /*
     * Searches all books by Author
     * 
     * @param value - the value we want to search for
     * 
     * @return - A list of all books matching the search
     */
    @GetMapping("search/author/{value}")
    List<Book> searchBooksByAuthor(@PathVariable String value) {
        value = value.replace("+", " ");
        return bookService.searchBooksByAuthor(value);
    }

    /*
     * Searches all books in a range of years
     * 
     * @param startYear - the start of the year range
     * 
     * @param endYear - the end of the year range
     * 
     * @return - A list of all books matching the search
     */
    @GetMapping("search/publicationYear/range/{startYear}/{endYear}")
    List<Book> searchBooksInYearRange(@PathVariable Integer startYear, @PathVariable Integer endYear) {
        return bookService.searchBooksInYearRange(startYear, endYear);
    }

    /*
     * Adds a book to the database
     * 
     * @param newBook - The book that we want to add
     * 
     * @return - The book that was just added
     */
    @PostMapping
    Book addBook(@Valid @RequestBody Book newBook) {
        return bookService.addBook(newBook);
    }

    /*
     * Get a book based on its isbn
     * 
     * @param isbn - The isbn that we want to search the database for
     * 
     * @return - The book that was being searched for
     */
    @GetMapping("/{isbn}")
    Book getByIsbn(@PathVariable @IsbnConstraint String isbn) throws BookNotFoundException {
        return bookService.getByIsbn(isbn);
    }

    /*
     * Update a book based on its isbn
     * 
     * @param isbn - The isbn that we want to update
     * 
     * @param updatedBook - A new copy of the book we want to update the record in
     * the database with
     * 
     * @return - The book that was updated
     */
    @PutMapping("/{isbn}")
    Book updateBook(@Valid @RequestBody Book updatedBook, @PathVariable @IsbnConstraint String isbn)
            throws BookNotFoundException {
        return bookService.updateBook(updatedBook, isbn);
    }

    /*
     * Update the books status allowing a book to be borrowed/returned
     * 
     * @param isbn - The isbn of the book that we want to borrow/return
     * 
     * @param newStatus - The status we want to update the book with
     * 
     * @return - The book that was updated
     */
    @PutMapping("/{isbn}/updateStatus/{newStatus}")
    Book updateBookStatus(@PathVariable @IsbnConstraint String isbn, @PathVariable BookStatus newStatus)
            throws Exception {
        return bookService.updateBookStatus(isbn, newStatus);
    }

    /*
     * Delete a book from the database
     * 
     * @param isbn - The isbn of the book that we want to delete
     */
    @DeleteMapping("/{isbn}")
    void deleteBookByIsbn(@PathVariable @IsbnConstraint String isbn) {
        bookService.deleteBookByIsbn(isbn);
    }

}
