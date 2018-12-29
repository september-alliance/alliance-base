package org.september.smartdao.model;

public class DuplicateValueException extends RuntimeException {

	private String value;
	
	public DuplicateValueException(String msg, String value, Exception ex) {
		super(msg, ex);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 636584666928820636L;

}
