package scopeland.libraryapp.exceptions.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import scopeland.libraryapp.exceptions.BookAlreadyExistsException;

/*
 * API Feedback for the BookAlreadyExistsException
 */
@ControllerAdvice
public class BookAlreadyExistsAdvice {
    @ResponseBody
    @ExceptionHandler(BookAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity bookNotFoundHandler(BookAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
