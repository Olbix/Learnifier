package pl.wlodarski.learnifier.upload.domain;

import lombok.Builder;
import lombok.Data;
import pl.wlodarski.learnifier.metadata.domain.Metadata;

import java.util.UUID;

@Data
@Builder
public class Upload {
    private UUID id;
    private byte[] content;
    private Metadata metadata;
}