package scopeland.libraryapp.service;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import scopeland.libraryapp.entities.Book;
import scopeland.libraryapp.enums.BookStatus;
import scopeland.libraryapp.exceptions.BookAlreadyBorrowedException;
import scopeland.libraryapp.exceptions.BookAlreadyExistsException;
import scopeland.libraryapp.exceptions.BookNotFoundException;
import scopeland.libraryapp.exceptions.SearchByYearsEndBeforeStartException;
import scopeland.libraryapp.repositories.interfaces.IBookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private IBookRepository bookRepository;

    public BookServiceImpl(IBookRepository bookRepository) {
        this.bookRepository = bookRepository;

    }

    /*
     * Gets a list of all stored books
     * 
     * @return - A list of all books stoed in the db
     */
    @Override
    public List<Book> listBooks() {
        return bookRepository.findAll();
    }

    /*
     * Searches all books by Author
     * 
     * @param value - the value we want to search for
     * 
     * @return - A list of all books matching the search
     */
    @Override
    public List<Book> searchBooksByAuthor(String value) {
        Example<Book> searchBook = Example.of(new Book());
        searchBook = Example.of(new Book(null, null, value, null, null));
        return bookRepository.findAll(searchBook);
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
    @Override
    public List<Book> searchBooksInYearRange(Integer startYear, Integer endYear) {
        if (startYear > endYear) {
            throw new SearchByYearsEndBeforeStartException(startYear, endYear);
        }
        return bookRepository.searchBooksInYearRange(startYear, endYear);
    }

    /*
     * Adds a book to the database
     * 
     * @param newBook - The book that we want to add
     * 
     * @return - The book that was just added
     */
    @Override
    public Book addBook(Book newBook) {

        Optional<Book> book = bookRepository.findById(newBook.getIsbn());
        if (!book.isPresent()) {
            newBook.setAuthor(Jsoup.clean(newBook.getAuthor(), Safelist.basic()));
            newBook.setTitle(Jsoup.clean(newBook.getTitle(), Safelist.basic()));
            return (bookRepository.save(newBook));
        } else {
            throw new BookAlreadyExistsException(newBook.getIsbn());
        }
    }

    /*
     * Get a book based on its isbn
     * 
     * @param isbn - The isbn that we want to search the database for
     * 
     * @return - The book that was being searched for
     */
    @Override
    public Book getByIsbn(String isbn) throws BookNotFoundException {
        Optional<Book> book = bookRepository.findById(isbn);
        if (book.isPresent()) {
            return book.get();
        } else {
            throw new BookNotFoundException(isbn);
        }
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
    @Override
    public Book updateBook(Book updatedBook, String isbn)
            throws BookNotFoundException {
        Optional<Book> book = bookRepository.findById(isbn);
        if (book.isPresent()) {
            updatedBook.setAuthor(Jsoup.clean(updatedBook.getAuthor(), Safelist.basic()));
            updatedBook.setTitle(Jsoup.clean(updatedBook.getTitle(), Safelist.basic()));
            return bookRepository.save(updatedBook);
        } else {
            throw new BookNotFoundException(isbn);
        }
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
    @Override
    public Book updateBookStatus(String isbn, BookStatus newStatus) throws Exception {
        Optional<Book> book = bookRepository.findById(isbn);
        if (book.isPresent()) {
            var updatedBook = book.get();
            if (newStatus == BookStatus.BORROWED && updatedBook.getBookStatus() == BookStatus.BORROWED) {
                throw new BookAlreadyBorrowedException(isbn); // Already taken out
            }
            updatedBook.setBookStatus(newStatus);
            return bookRepository.save(updatedBook);
        } else {
            throw new BookNotFoundException(isbn);
        }
    }

    /*
     * Delete a book from the database
     * 
     * @param isbn - The isbn of the book that we want to delete
     */
    @Override
    public void deleteBookByIsbn(String isbn) {
        Optional<Book> book = bookRepository.findById(isbn);
        if (book.isPresent()) {
            bookRepository.deleteById(isbn);
        } else {
            throw new BookNotFoundException(isbn);
        }
    }
}
