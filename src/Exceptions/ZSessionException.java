package Exceptions;

public class ZSessionException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ZSessionException(){}

    public ZSessionException(String message) {
        super(message);
    }

    public ZSessionException(String message, Throwable throwable) {
        super(message, throwable);
    }

}

