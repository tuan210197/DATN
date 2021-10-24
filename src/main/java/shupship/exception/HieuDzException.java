package shupship.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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