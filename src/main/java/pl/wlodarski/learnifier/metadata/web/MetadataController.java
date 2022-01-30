package pl.wlodarski.learnifier.metadata.web;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wlodarski.learnifier.metadata.application.port.MetadataService;
import pl.wlodarski.learnifier.metadata.domain.Metadata;
import pl.wlodarski.learnifier.upload.application.port.UploadService;
import pl.wlodarski.learnifier.upload.domain.Upload;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/metadata")
public class MetadataController {
    private static final String EMPTY_JSON = "{}";
    private static final String METADATA_RESPONSE_FILTER = "metadataResponse";
    MetadataService metadataService;
    UploadService uploadService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMetadata(@PathVariable final UUID id, @RequestParam(required = false) final Set<String> fields) throws JsonProcessingException {
        final Optional<Metadata> metadata = loadMetadata(id);
        if (metadata.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final String response = obtainResponseFromMapper(prepareObjectMapperWithFieldsDependedFilter(fields), toMetadataResponse(metadata.get()));
        return ResponseEntity.ok(response);
    }

    private Optional<Metadata> loadMetadata(final UUID id) {
        final Optional<Upload> upload = uploadService.getById(id);
        if (upload.isEmpty()) {
            return Optional.empty();
        }
        return metadataService.obtainUploadMetadata(upload.get());
    }

    private String obtainResponseFromMapper(final ObjectMapper om, final MetadataResponse metadataResponse) throws JsonProcessingException {
        final String response = om.writeValueAsString(metadataResponse);
        if (EMPTY_JSON.equals(response)) {
            throw new FailedToExtractRequestedFieldsException("Requested fields does not exist!: Chose at least one of: fileName, size, contentType");
        } else {
            return response;
        }
    }

    private ObjectMapper prepareObjectMapperWithFieldsDependedFilter(final Set<String> fields) {
        final ObjectMapper mapper = new ObjectMapper();
        final SimpleFilterProvider filters = obtainFieldsFilter(fields);
        mapper.setFilterProvider(filters);
        return mapper;
    }

    private SimpleFilterProvider obtainFieldsFilter(final Set<String> fields) {
        final SimpleBeanPropertyFilter simpleBeanPropertyFilter;
        if (Objects.isNull(fields)) {
            simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAll();
        } else {
            simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
        }
        return new SimpleFilterProvider().addFilter(
                METADATA_RESPONSE_FILTER, simpleBeanPropertyFilter);
    }

    private MetadataResponse toMetadataResponse(final Metadata metadata) {
        return new MetadataResponse(metadata.getFileName(), metadata.getSize(), metadata.getContentType());
    }

    @JsonFilter(METADATA_RESPONSE_FILTER)
    @AllArgsConstructor
    @Getter
    static class MetadataResponse {
        String fileName;
        Long size;
        String contentType;
    }
}
