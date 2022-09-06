package managers.exceptions;

public class IntersectionTaskException extends RuntimeException {

    public IntersectionTaskException() {
    }

    public IntersectionTaskException(String message) {
        super(message);
    }

    public IntersectionTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public IntersectionTaskException(Throwable cause) {
        super(cause);
    }
}
