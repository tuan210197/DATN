package shupship.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

import javax.persistence.NonUniqueResultException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<Object> handleException(HieuDzException ex) {
        return handleMessageException(ex);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<Object> handleException(MissingServletRequestParameterException ex) {
        return handleMessageException(ex);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<Object> handleException(NullPointerException ex) {
        return handleMessageException(ex);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<Object> handleException(IllegalArgumentException ex) {
        return handleMessageException(ex);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<Object> handleException(DateTimeParseException ex) {
        return handleMessageException(ex);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<Object> handleException(NonUniqueResultException ex) {
        return handleMessageException(ex);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<Object> handleException(MultipartException ex) {
        return handleMessageException(ex);
    }


    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {
        return handleMessageException(ex);
    }

    private ResponseEntity<Object> handleMessageException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "true");
        body.put("status", 69);
        body.put("data", null);
        if (ex instanceof HieuDzException) {
            HieuDzException e = (HieuDzException) ex;
            body.put("message", e.getErrorMessage());
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        else if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
            List<String> errors = e.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            body.put("message", errors);
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        else if (ex instanceof IllegalArgumentException) {
            IllegalArgumentException e = (IllegalArgumentException) ex;
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        else if (ex instanceof NullPointerException || ex instanceof NonUniqueResultException) {
            body.put("message", ex.getMessage());
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        else if (ex instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException e = (MissingServletRequestParameterException) ex;
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        else if (ex instanceof DateTimeParseException) {
            DateTimeParseException e = (DateTimeParseException) ex;
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        else if (ex instanceof MultipartException) {
            body.put("message", "File lỗi !!! ");
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        else {
            body.put("message", ex.getMessage());
        }
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}