package pl.wlodarski.learnifier.upload.application.port;

import lombok.AllArgsConstructor;
import lombok.Value;
import pl.wlodarski.learnifier.metadata.domain.Metadata;
import pl.wlodarski.learnifier.upload.domain.Upload;

import java.util.Optional;
import java.util.UUID;

public interface UploadService {
    Upload save(SaveUploadCommand command);

    UpdateStatus modify(ModifyUploadCommand command);

    Optional<Upload> getById(UUID uuid);

    boolean removeById(UUID uuid);

    enum UpdateStatus {
        NOT_MODIFIED, MODIFIED, NOT_FOUND
    }

    @Value
    @AllArgsConstructor
    class SaveUploadCommand {
        byte[] content;
        Metadata metadata;
    }

    @Value
    @AllArgsConstructor
    class ModifyUploadCommand {
        UUID id;
        byte[] content;
        Metadata metadata;
    }
}
