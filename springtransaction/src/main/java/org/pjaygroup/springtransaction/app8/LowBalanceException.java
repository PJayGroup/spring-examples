/**
 * 
 */
package org.pjaygroup.springtransaction.app8;

/**
 * @author Vijay Konduru
 *
 */
public class LowBalanceException extends RuntimeException {

	private static final long serialVersionUID = -7490762523318814161L;

	public LowBalanceException() {
		super();
	}
	
	public LowBalanceException(String message){
		super(message);
	}
}
