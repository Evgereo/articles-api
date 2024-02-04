package world.evgereo.articles.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import world.evgereo.articles.errors.exceptions.AuthException;
import world.evgereo.articles.errors.exceptions.DuplicateUserException;
import world.evgereo.articles.errors.exceptions.PasswordMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validationErrorsHandler(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(
                ex.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, fieldError -> {
                    String message = fieldError.getDefaultMessage();
                    return message != null ? message : "Default message doesn't exist";
                })),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HashMap<String, String>> badAuthenticationHandler(BadCredentialsException ex) {
        return new ResponseEntity<>(new HashMap<>(Map.of("message", ex.getMessage())), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<HashMap<String, String>> authHandler(AuthException ex) {
        System.out.println(ex.getMessage());
        return new ResponseEntity<>(new HashMap<>(Map.of("message", ex.getMessage())), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<HashMap<String, String>> duplicateUserHandler(DuplicateUserException ex) {
        return new ResponseEntity<>(new HashMap<>(Map.of("message", ex.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<HashMap<String, String>> passwordMismatchHandler(PasswordMismatchException ex) {
        return new ResponseEntity<>(new HashMap<>(Map.of("message", ex.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<HashMap<String, String>> userNotFoundHandler(UsernameNotFoundException ex) {
        return new ResponseEntity<>(new HashMap<>(Map.of("message", ex.getMessage())), HttpStatus.BAD_REQUEST);
    }
}