package shupship.domain.message;

import java.io.Serializable;

/**
 * Class name.
 *
 * @author tuandv
 * @version 1.0
 * @since 02/10/2021
 */
public class BaseMessage implements Serializable {
    private String code;
    private String error;
    private String description;
    private String message;

    public BaseMessage() {
    }

    public BaseMessage(String code, String error, String description) {
        this.code = code;
        this.description = description;
        this.error = error;
    }

    public BaseMessage(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
