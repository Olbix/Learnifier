package pl.wlodarski.learnifier.metadata.web;

public class FailedToExtractRequestedFieldsException extends RuntimeException {
    public FailedToExtractRequestedFieldsException(final String msg) {
        super(msg);
    }
}
