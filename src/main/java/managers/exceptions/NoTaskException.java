package managers.exceptions;

public class NoTaskException extends RuntimeException {
    
    public NoTaskException() {
    }

    public NoTaskException(String message) {
        super(message);
    }

    public NoTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoTaskException(Throwable cause) {
        super(cause);
    }
}
