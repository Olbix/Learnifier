package pl.wlodarski.learnifier.metadata.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class Metadata {
    private final String fileName;
    private final Long size;
    private final String contentType;
}
