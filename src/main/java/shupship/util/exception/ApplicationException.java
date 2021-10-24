package shupship.util.exception;


<<<<<<< HEAD
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


=======
import lombok.Data;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.util.StringUtils;
import shupship.common.Const;

/**
 * @author MSI
 */
@Data
public class ApplicationException extends Exception {

    private int code;
    private String errorCode;
    private String message;
    private String language;
    private String attack;
    private boolean hasAttack = false;
    private ErrorMessage errorMessage;
    public ApplicationException(int code) {
        this.code = code;
    }

    public ApplicationException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public ApplicationException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApplicationException(int code, String attack, boolean hasAttack) {
        this.code = code;
        this.attack = attack;
        this.hasAttack = hasAttack;
    }

    public ApplicationException(int code, String message, String language) {
        this.code = code;
        this.message = this.getMessage(language) + " " + message;
    }

    public ApplicationException(int codeBss, int code, String message, String language) {
        this.code = codeBss;
        this.message = this.getMessage(language) + ": '" + code + ": " + message + "'";
    }

    @Override
    public String getMessage() {
        if (!StringUtils.hasText(message)) {
            return Const.getMessage(code);
        }
        return message;
    }

    public String getMessage(String language) {
        if (!StringUtils.hasText(message)) {
            return Const.getMessage(code);
        }
        return message;
    }
>>>>>>> origin/dev_tungtt

}
