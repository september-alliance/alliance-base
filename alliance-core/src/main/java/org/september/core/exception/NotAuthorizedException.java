package org.september.core.exception;

public class NotAuthorizedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3237155572743553048L;

	public NotAuthorizedException(String msg){
		super(msg);
	}
	
	public NotAuthorizedException(String msg , Throwable ex){
		super(msg , ex);
	}
}
