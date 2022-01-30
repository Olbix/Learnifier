package pl.wlodarski.learnifier.upload.application.exception;

public class IllegalContentTypeException extends RuntimeException {
    public IllegalContentTypeException(final String message) {
        super(message);
    }
}
