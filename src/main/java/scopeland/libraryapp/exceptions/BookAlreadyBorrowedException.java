package scopeland.libraryapp.exceptions;

/*
 * An exception to throw if a specified has been borrowed already and can't be borrowed again
 */
public class BookAlreadyBorrowedException extends RuntimeException {
    public BookAlreadyBorrowedException(String isbn) {
        super("Book " + isbn + "Already Borrowed");
    }
}