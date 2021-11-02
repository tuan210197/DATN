package shupship.util.exception;

public class BusinessException extends RuntimeException {
    private ErrorMessage errorMessage;

    public BusinessException(ErrorMessage errorMessages) {
        this.errorMessage = errorMessages;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}