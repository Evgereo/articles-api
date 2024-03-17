package world.evgereo.articles.errors.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String massage) {
        super(massage);
    }
}
