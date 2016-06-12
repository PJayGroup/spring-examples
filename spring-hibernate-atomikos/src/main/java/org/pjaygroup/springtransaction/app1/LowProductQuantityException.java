/**
 * 
 */
package org.pjaygroup.springtransaction.app1;

/**
 * @author Vijay Konduru
 *
 */
public class LowProductQuantityException extends RuntimeException{
	
	private static final long serialVersionUID = 3715366766104395584L;
	
	public LowProductQuantityException() {
		super();
	}
	
	public LowProductQuantityException(String message) {
		super(message);
	}

}
