package world.evgereo.articles.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validationErrorsHandler(MethodArgumentNotValidException ex) {
        Stream<String> fieldErrors = ex.getFieldErrors().stream()
            .map(e -> String.format("%s: %s", e.getField(), e.getDefaultMessage()));
        Stream<String> globalErrors = ex.getGlobalErrors().stream()
            .map(e -> String.format("%s: %s", e.getObjectName(), e.getDefaultMessage()));
        String errors = Stream.concat(fieldErrors, globalErrors)
            .collect(Collectors.joining(", "));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
