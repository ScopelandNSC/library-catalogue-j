package scopeland.libraryapp.exceptions;

/*
 * An exception to throw if a specified booking already exists when it shouldn't
 */
public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String isbn) {
        super("Book " + isbn + "already Exists");
    }
}