package Exceptions;

public class CounterException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CounterException(){}

    public CounterException(String message) {
        super(message);
    }

    public CounterException(String message, Throwable throwable) {
        super(message, throwable);
    }

}

