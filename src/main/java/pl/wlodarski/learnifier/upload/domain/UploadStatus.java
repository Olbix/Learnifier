package pl.wlodarski.learnifier.upload.domain;

public enum UploadStatus {
    UPLOADING {
        @Override
        public UpdateStatusResult updateStatus(UploadStatus status) {
            switch (status) {
                case READY:
                    return UpdateStatusResult.ok(READY);
                default:
                    return super.updateStatus(status);
            }
        }
    },
    PROCESSING {
        @Override
        public UpdateStatusResult updateStatus(UploadStatus status) {
            if (status == READY) {
                return UpdateStatusResult.ok(READY);
            }
            return super.updateStatus(status);
        }
    },
    READY {
        @Override
        public UpdateStatusResult updateStatus(UploadStatus status) {
            switch (status) {
                case PROCESSING:
                    return UpdateStatusResult.ok(PROCESSING);
                case DELETING:
                    return UpdateStatusResult.ok(DELETING);
                default:
                    return super.updateStatus(status);
            }
        }
    },
    DELETING {
        @Override
        public UpdateStatusResult updateStatus(UploadStatus status) {
            if (status == DELETED) {
                return UpdateStatusResult.ok(DELETED);
            }
            return super.updateStatus(status);
        }
    },
    DELETED;

    @Override
    public String toString() {
        return this.name();
    }

    public UpdateStatusResult updateStatus(UploadStatus status) {
        throw new IllegalArgumentException("Unable to mark " + this.name() + " order as " + status.name());
    }
}
