package shupship.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import shupship.common.Const;
import shupship.domain.message.ResponseMessage;
import shupship.domain.model.Users;
<<<<<<<<< Temporary merge branch 1
import shupship.util.exception.ApplicationException;
=========
import shupship.util.exception.ApiException;
>>>>>>>>> Temporary merge branch 2

/**
 * CommonController
 *
 * @author tuandv
 * @version 1.0
 * @since 02/10/2021
 */
public abstract class BaseController {

    protected <T> ResponseEntity<?> toSuccessResult(T data, String successMessage) {
        ResponseMessage<T> message = new ResponseMessage<>();

        message.setCode(Const.API_RESPONSE.RETURN_CODE_SUCCESS + "");
        message.setSuccess(Const.API_RESPONSE.RESPONSE_STATUS_TRUE);
        message.setMessage(successMessage);
        message.setData(data);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    protected <T> ResponseEntity<?> toExceptionResult(String errorMessage, int code) {
        ResponseMessage<T> message = new ResponseMessage<>();

        message.setSuccess(Const.API_RESPONSE.RESPONSE_STATUS_FALSE);
        message.setCode(code + "");
        message.setMessage(errorMessage);

        return new ResponseEntity<>(message, HttpStatus.valueOf(code));
    }

<<<<<<<<< Temporary merge branch 1
    protected Users getCurrentUser() throws Exception {
        Users user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null) {
            throw new ApplicationException("User is null");
        }
        return user;

    }
=========
>>>>>>>>> Temporary merge branch 2
}
