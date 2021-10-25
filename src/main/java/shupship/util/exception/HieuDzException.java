package shupship.util.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HieuDzException extends RuntimeException {
    private String errorMessage;
}