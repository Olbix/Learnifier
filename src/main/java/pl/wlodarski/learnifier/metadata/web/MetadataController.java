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

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMetadata(@PathVariable UUID id, @RequestParam(required = false) Set<String> fields) throws JsonProcessingException {
        Optional<Metadata> metadata = metadataService.obtainUploadMetadataById(id);
        if (metadata.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String response = obtainResponseFromMapper(prepareObjectMapperWithFieldsDependedFilter(fields), toMetadataResponse(metadata.get()));
        return ResponseEntity.ok(response);
    }

    private String obtainResponseFromMapper(ObjectMapper om, MetadataResponse metadataResponse) throws JsonProcessingException {
        String response = om.writeValueAsString(metadataResponse);
        if (EMPTY_JSON.equals(response)) {
            throw new FailedToExtractRequestedFieldsException("Requested fields does not exist!: Chose at least one of: fileName, size, contentType");
        } else {
            return response;
        }
    }

    private ObjectMapper prepareObjectMapperWithFieldsDependedFilter(Set<String> fields) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleFilterProvider filters = obtainFieldsFilter(fields);
        mapper.setFilterProvider(filters);
        return mapper;
    }

    private SimpleFilterProvider obtainFieldsFilter(Set<String> fields) {
        SimpleBeanPropertyFilter simpleBeanPropertyFilter;
        if (Objects.isNull(fields)) {
            simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAll();
        } else {
            simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
        }
        return new SimpleFilterProvider().addFilter(
                METADATA_RESPONSE_FILTER, simpleBeanPropertyFilter);
    }

    private MetadataResponse toMetadataResponse(Metadata metadata) {
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
