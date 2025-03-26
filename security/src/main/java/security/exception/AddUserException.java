package security.exception;

public class AddUserException extends RuntimeException {
    private String message;
    public AddUserException(String message){
        super(message);
    }
}
