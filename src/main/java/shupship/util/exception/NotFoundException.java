package shupship.util.exception;

public class NotFoundException extends RuntimeException {
    private ErrorMessage errorMessage;

    public NotFoundException(ErrorMessage errorMessages) {
        this.errorMessage = errorMessages;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}