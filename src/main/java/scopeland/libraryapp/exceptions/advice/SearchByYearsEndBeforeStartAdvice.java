package scopeland.libraryapp.exceptions.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import scopeland.libraryapp.exceptions.SearchByYearsEndBeforeStartException;

/*
 * API Feedback for the SearchByYearsEndBeforeStartException
 */
@ControllerAdvice
public class SearchByYearsEndBeforeStartAdvice {
    @ResponseBody
    @ExceptionHandler(SearchByYearsEndBeforeStartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity bookNotFoundHandler(SearchByYearsEndBeforeStartException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
