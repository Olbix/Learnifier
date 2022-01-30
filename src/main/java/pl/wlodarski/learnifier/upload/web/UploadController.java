package pl.wlodarski.learnifier.upload.web;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.wlodarski.learnifier.metadata.application.port.MetadataService;
import pl.wlodarski.learnifier.metadata.domain.Metadata;
import pl.wlodarski.learnifier.upload.application.port.UploadService;
import pl.wlodarski.learnifier.upload.application.port.UploadService.ModifyUploadCommand;
import pl.wlodarski.learnifier.upload.application.port.UploadService.SaveUploadCommand;
import pl.wlodarski.learnifier.upload.application.port.UploadService.UpdateStatus;
import pl.wlodarski.learnifier.upload.domain.Upload;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/upload")
public class UploadController {

    private static final String CONTENT_TYPE = "Content-Type";
    private final UploadService uploadService;
    private final MetadataService metadataService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveUpload(@RequestPart("file") MultipartFile file) {
        Upload upload = uploadService.save(toSaveCommand(file));

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(upload.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PatchMapping(value = {"{id}"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> adjustUpload(@PathVariable UUID id, @RequestPart("file") MultipartFile file) {
        UpdateStatus update = uploadService.modify(toModifyUploadCommand(id, file));
        if (update == UpdateStatus.NOT_FOUND) {
            return ResponseEntity.notFound().build();
        }
        if (update == UpdateStatus.NOT_MODIFIED) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}/file")
    public ResponseEntity<Resource> getUploadFile(@PathVariable String id) {
        Optional<Upload> optionalUpload = uploadService.getById(UUID.fromString(id));
        if (optionalUpload.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Upload upload = optionalUpload.get();
        Metadata metadata = upload.getMetadata();

        String contentDisposition = "attachment; filename=\"" + metadata.getFileName() + "\"";
        Resource resource = new ByteArrayResource(upload.getContent());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.parseMediaType(metadata.getContentType()))
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUpload(@PathVariable UUID id) {
        boolean removed = uploadService.removeById(id);
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @SneakyThrows
    private ModifyUploadCommand toModifyUploadCommand(UUID id, MultipartFile file) {
        Metadata fileMetadata = metadataService.readMetadata(file);
        return new ModifyUploadCommand(id, file.getBytes(), fileMetadata);
    }

    @SneakyThrows
    SaveUploadCommand toSaveCommand(MultipartFile file) {
        Metadata fileMetadata = metadataService.readMetadata(file);
        return new SaveUploadCommand(file.getBytes(), fileMetadata);
    }

    @Value
    @AllArgsConstructor
    static class UploadResponse {
        UUID id;
        String contentType;
        String filename;
    }

}
