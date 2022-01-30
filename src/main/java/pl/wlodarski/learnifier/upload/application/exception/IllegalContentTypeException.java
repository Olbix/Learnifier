package pl.wlodarski.learnifier.upload.application.exception;

public class IllegalContentTypeException extends RuntimeException {
    public IllegalContentTypeException(String message) {
        super(message);
    }
}
