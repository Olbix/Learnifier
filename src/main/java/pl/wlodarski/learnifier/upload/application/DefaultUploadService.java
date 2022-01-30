package pl.wlodarski.learnifier.upload.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wlodarski.learnifier.metadata.domain.Metadata;
import pl.wlodarski.learnifier.upload.application.exception.IllegalContentTypeException;
import pl.wlodarski.learnifier.upload.application.port.UploadService;
import pl.wlodarski.learnifier.upload.db.InMemoryStorage;
import pl.wlodarski.learnifier.upload.domain.Upload;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DefaultUploadService implements UploadService {

    private final InMemoryStorage inMemoryStorage;
    private final UploadProperties uploadProperties;

    @Override
    public Upload save(final SaveUploadCommand command) {
        final Metadata metadata = command.getMetadata();
        validateMetadata(metadata);

        return inMemoryStorage.upload(toUpload(command));
    }

    private void validateMetadata(final Metadata metadata) {
        final String extension = metadata.getContentType();
        if (!isContentTypeSupported(extension)) {
            throw new IllegalContentTypeException("Uploading: " + extension + " content type is not supported!");
        }
    }

    @Override
    public UpdateStatus modify(final ModifyUploadCommand command) {
        final UUID id = command.getId();
        final Optional<Upload> optionalUpload = inMemoryStorage.getById(id);
        if (optionalUpload.isEmpty()) {
            return UpdateStatus.NOT_FOUND;
        }

        final Upload toBeModified = optionalUpload.get();
        if (sameMetadata(command, toBeModified)) {
            toBeModified.setContent(command.getContent());
            toBeModified.setMetadata(command.getMetadata());
            return UpdateStatus.MODIFIED;
        }
        return UpdateStatus.NOT_MODIFIED;
    }

    private boolean sameMetadata(final ModifyUploadCommand command, final Upload toBeModified) {
        return command.getMetadata().getContentType().equals(toBeModified.getMetadata().getContentType());
    }

    private boolean isContentTypeSupported(final String contentType) {
        return uploadProperties.getSupportedTypesAsList().contains(contentType);
    }

    @Override
    public Optional<Upload> getById(final UUID uuid) {
        return inMemoryStorage.getById(uuid);
    }

    @Override
    public boolean removeById(final UUID uuid) {
        return inMemoryStorage.remove(uuid);
    }


    private Upload toUpload(final SaveUploadCommand uploadCommand) {
        return Upload.builder()
                .content(uploadCommand.getContent())
                .metadata(uploadCommand.getMetadata())
                .id(UUID.randomUUID()).build();
    }
}
