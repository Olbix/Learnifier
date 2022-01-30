package pl.wlodarski.learnifier.metadata.application;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.wlodarski.learnifier.metadata.application.port.MetadataService;
import pl.wlodarski.learnifier.metadata.domain.Metadata;
import pl.wlodarski.learnifier.upload.domain.Upload;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class DefaultMetadataService implements MetadataService {

    private final Tika tika = new Tika();

    @Override
    public Metadata readMetadata(final MultipartFile file) {
        return Metadata.builder()
                .contentType(obtainContentType(file.getContentType(), detectedMimeType(file)))
                .fileName(file.getOriginalFilename())
                .size(file.getSize())
                .build();
    }

    @Override
    public Optional<Metadata> obtainUploadMetadata(final Upload upload) {
        return Optional.of(upload.getMetadata());
    }


    private String obtainContentType(final String fileContentType, final String detectedMimeType) {
        if (StringUtils.isBlank(fileContentType)) {
            return detectedMimeType;
        }
        if (StringUtils.equals(fileContentType, detectedMimeType)) {
            return fileContentType;
        }
        log.warn("Incoming content type might been tampered: {} ,detected mime is: {}", fileContentType, detectedMimeType);
        return fileContentType;
    }

    private String detectedMimeType(final MultipartFile file) {
        String detect = null;
        try {
            detect = tika.detect(file.getBytes());
        } catch (final IOException e) {
            log.debug("Cannot detect content type", e);
        }
        return detect;
    }

}
