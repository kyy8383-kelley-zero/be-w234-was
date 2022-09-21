package error;

public class FailedLoginException extends RuntimeException{
    public FailedLoginException(String message){
        super(message);
    }
}
