package pl.wlodarski.learnifier.metadata.domain;

import lombok.Value;

@Value
public class UpdateStatusResult {
    UploadStatus newStatus;

    public static UpdateStatusResult ok(final UploadStatus newStatus) {
        return new UpdateStatusResult(newStatus);
    }
}
