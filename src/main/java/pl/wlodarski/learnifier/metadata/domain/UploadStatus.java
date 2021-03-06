package pl.wlodarski.learnifier.metadata.domain;

public enum UploadStatus {
    UPLOADING {
        @Override
        public UpdateStatusResult updateStatus(final UploadStatus status) {
            if (status == UploadStatus.READY) {
                return UpdateStatusResult.ok(READY);
            }
            return super.updateStatus(status);
        }
    },
    PROCESSING {
        @Override
        public UpdateStatusResult updateStatus(final UploadStatus status) {
            if (status == READY) {
                return UpdateStatusResult.ok(READY);
            }
            return super.updateStatus(status);
        }
    },
    READY {
        @Override
        public UpdateStatusResult updateStatus(final UploadStatus status) {
            return switch (status) {
                case PROCESSING -> UpdateStatusResult.ok(PROCESSING);
                case DELETING -> UpdateStatusResult.ok(DELETING);
                default -> super.updateStatus(status);
            };
        }
    },
    DELETING {
        @Override
        public UpdateStatusResult updateStatus(final UploadStatus status) {
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

    public UpdateStatusResult updateStatus(final UploadStatus status) {
        throw new IllegalArgumentException("Unable to mark " + this.name() + " order as " + status.name());
    }
}
