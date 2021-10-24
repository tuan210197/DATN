package shupship.util.exception;


public class HieuDzException extends RuntimeException {
    private ErrorMessage errorMessage;

    public HieuDzException(ErrorMessage errorMessage) {
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }
}