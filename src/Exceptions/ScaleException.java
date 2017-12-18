package Exceptions;

public class ScaleException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ScaleException(){}

    public ScaleException(String message) {
        super(message);
    }

    public ScaleException(String message, Throwable throwable) {
        super(message, throwable);
    }

}

