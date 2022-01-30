package pl.wlodarski.learnifier.upload.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.wlodarski.learnifier.upload.application.exception.IllegalContentTypeException;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class UploadExceptionHandler {

    @ExceptionHandler(IllegalContentTypeException.class)
    public ResponseEntity<Object> handleIllegalContentTypeException(final IllegalContentTypeException ex) {
        return handleError(HttpStatus.BAD_REQUEST, List.of(ex.getMessage()));
    }

    private ResponseEntity<Object> handleError(final HttpStatus status, final List<String> errors) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("errors", errors);
        return new ResponseEntity<>(body, status);
    }
}
