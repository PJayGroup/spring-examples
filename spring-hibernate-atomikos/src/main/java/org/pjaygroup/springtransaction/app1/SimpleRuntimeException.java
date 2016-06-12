/**
 * 
 */
package org.pjaygroup.springtransaction.app1;

/**
 * @author Vijay Konduru
 *
 */
public class SimpleRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = -6594828367666435239L;
	
	public SimpleRuntimeException() {
		super();
	}
	
	public SimpleRuntimeException(String message) {
		super(message);
	}

}
