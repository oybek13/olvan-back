package brb.team.olvanback.exception;

import brb.team.olvanback.dto.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<?> dataNotFoundException(DataNotFoundException exception) {
        return new ResponseEntity<>(new CommonResponse<>(false, exception.getMessage(), null), HttpStatus.NOT_FOUND);
    }
}
