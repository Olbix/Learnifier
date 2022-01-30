package pl.wlodarski.learnifier.metadata.domain;

import lombok.Value;

@Value
public class UpdateStatusResult {
    UploadStatus newStatus;
    boolean revoked;

    static UpdateStatusResult ok(UploadStatus newStatus) {
        return new UpdateStatusResult(newStatus, false);
    }
}
