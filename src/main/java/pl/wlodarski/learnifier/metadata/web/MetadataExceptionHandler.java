package pl.wlodarski.learnifier.metadata.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class MetadataExceptionHandler {

    @ExceptionHandler(FailedToExtractRequestedFieldsException.class)
    public ResponseEntity<Object> handleIllegalContentTypeException(FailedToExtractRequestedFieldsException ex) {
        return handleError(HttpStatus.PRECONDITION_FAILED, List.of(ex.getMessage()));
    }

    private ResponseEntity<Object> handleError(HttpStatus status, List<String> errors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("errors", errors);
        return new ResponseEntity<>(body, status);
    }
}
