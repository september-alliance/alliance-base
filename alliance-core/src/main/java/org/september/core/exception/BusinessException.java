package org.september.core.exception;

public class BusinessException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3676391735019383501L;

	private String code;
	
	public BusinessException(String msg){
		super(msg);
	}
	
	public BusinessException(String msg , Throwable ex){
		super(msg , ex);
	}

	public String getCode() {
		return code;
	}
	
	
	public BusinessException(String msg , String code){
		super(msg);
		this.code = code;
	}
	
	public BusinessException(String msg , String code , Throwable ex){
		super(msg , ex);
		this.code = code;
	}
}
