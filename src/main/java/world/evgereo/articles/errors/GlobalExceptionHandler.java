package world.evgereo.articles.errors;

import lombok.extern.slf4j.Slf4j;
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
import world.evgereo.articles.errors.exceptions.NotFoundException;
import world.evgereo.articles.errors.exceptions.PasswordMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validationErrorsHandler(MethodArgumentNotValidException ex) {
        return !ex.getFieldErrors().getFirst().getField().equals("refreshToken") ?
                new ResponseEntity<>(
                        ex.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, fieldError -> {
                            String message = fieldError.getDefaultMessage();
                            GlobalExceptionHandler.log.debug(message);
                            return message != null ? message : "Default message doesn't exist";
                        })), HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HashMap<String, String>> badAuthenticationHandler(BadCredentialsException ex) {
        GlobalExceptionHandler.log.debug(ex.getMessage());
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<HashMap<String, String>> authHandler(AuthException ex) {
        GlobalExceptionHandler.log.debug(ex.getMessage());
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map<String, String>> duplicateUserHandler(DuplicateUserException ex) {
        GlobalExceptionHandler.log.debug(ex.getMessage());
        return new ResponseEntity<>(Map.of("message", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<Map<String, String>> passwordMismatchHandler(PasswordMismatchException ex) {
        GlobalExceptionHandler.log.debug(ex.getMessage());
        return new ResponseEntity<>(Map.of("message", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<HashMap<String, String>> userNotFoundHandler(UsernameNotFoundException ex) {
        GlobalExceptionHandler.log.debug(ex.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HashMap<String, String>> userNotFoundHandler(NotFoundException ex) {
        GlobalExceptionHandler.log.debug(ex.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
