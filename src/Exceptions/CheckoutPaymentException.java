package Exceptions;

public class CheckoutPaymentException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CheckoutPaymentException(){}
	
	public CheckoutPaymentException(String message) {
        super(message);
    }

    public CheckoutPaymentException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
