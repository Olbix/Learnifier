package pl.wlodarski.learnifier.metadata.application.port;

import org.springframework.web.multipart.MultipartFile;
import pl.wlodarski.learnifier.metadata.domain.Metadata;

import java.util.Optional;
import java.util.UUID;

public interface MetadataService {
    Metadata readMetadata(MultipartFile file);

    Optional<Metadata> obtainUploadMetadataById(UUID upload);
}
