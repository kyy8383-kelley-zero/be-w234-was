package error;

public class DuplicatedUserException extends RuntimeException {
    public DuplicatedUserException(String message) {
        super(message);
    }
}
