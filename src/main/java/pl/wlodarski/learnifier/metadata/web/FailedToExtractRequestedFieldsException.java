package pl.wlodarski.learnifier.metadata.web;

public class FailedToExtractRequestedFieldsException extends RuntimeException {
    public FailedToExtractRequestedFieldsException(String msg) {
        super(msg);
    }
}
