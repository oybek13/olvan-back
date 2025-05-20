package brb.team.olvanback.exception;

import brb.team.olvanback.dto.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<?> dataNotFoundException(DataNotFoundException exception) {
        return new ResponseEntity<>(new CommonResponse<>(false, exception.getMessage(), null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<?>> handleAll(Exception e, HttpServletRequest req) {
        log.error("❌ Exception at [{} {}]: {}", req.getMethod(), req.getRequestURI(), e.getMessage(), e);
        if (e instanceof AuthenticationException || e instanceof AccessDeniedException) {
            // Pass, security already handled
            throw (RuntimeException) e;
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponse<>(false, "Error: " + e.getMessage(), null));
    }

}
