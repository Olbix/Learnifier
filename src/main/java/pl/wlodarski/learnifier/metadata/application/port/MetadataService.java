package pl.wlodarski.learnifier.metadata.application.port;

import org.springframework.web.multipart.MultipartFile;
import pl.wlodarski.learnifier.metadata.domain.Metadata;
import pl.wlodarski.learnifier.upload.domain.Upload;

import java.util.Optional;

public interface MetadataService {
    Metadata readMetadata(MultipartFile file);

    Optional<Metadata> obtainUploadMetadata(Upload upload);
}
