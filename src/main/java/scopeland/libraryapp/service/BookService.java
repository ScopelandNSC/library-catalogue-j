package scopeland.libraryapp.service;

import java.util.List;

import scopeland.libraryapp.entities.Book;
import scopeland.libraryapp.enums.BookStatus;

public interface BookService {
    List<Book> listBooks();

    List<Book> searchBooksByAuthor(String value);

    List<Book> searchBooksInYearRange(Integer startYear, Integer endYear);

    Book addBook(Book newBook);

    Book getByIsbn(String isbn);

    Book updateBook(Book updatedBook, String isbn);

    Book updateBookStatus(String isbn, BookStatus newStatus) throws Exception;

    void deleteBookByIsbn(String isbn);
}
