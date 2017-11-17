package Exceptions;

public class CheckoutGeneralException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CheckoutGeneralException(){}
	
	public CheckoutGeneralException(String message) {
        super(message);
    }

    public CheckoutGeneralException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
