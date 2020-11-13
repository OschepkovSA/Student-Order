package edu.java.studentorder.exception;

public class DaoException extends Exception {
	
	
	
	public DaoException() {
	}
	
	public DaoException(Exception e) {
	}
	
	public DaoException(String message) {
		super(message);
	}
	
	public DaoException(String message , Throwable cause) {
		super(message , cause);
	}
}
