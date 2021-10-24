package shupship.util.exception;


import java.util.ArrayList;
import java.util.List;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 5844188811840632808L;

	private String errorCode;


	public ApplicationException() {
		super();
	}

	public ApplicationException(String errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}

	public ApplicationException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}


	public ApplicationException(String errorCd, String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.errorCode = errorCd;
	}

	public ApplicationException(String errorCd, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCd;
	}

	public ApplicationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public String getErrorCd() {
		return errorCode;
	}

	public void setErrorCd(String errorCd) {
		this.errorCode = errorCd;
	}



}
