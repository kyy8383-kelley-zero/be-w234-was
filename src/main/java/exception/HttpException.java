package exception;

public class HttpException extends RuntimeException {
    public HttpException(HttpErrorMessage message) {
        super(message.getMessage());
    }
}
