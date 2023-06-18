package scopeland.libraryapp.exceptions;

/*
 * An exception to throw if a specified booking can't be found
 */
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String isbn) {
        super("Could not find book " + isbn);
    }
}