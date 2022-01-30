package pl.wlodarski.learnifier.upload.db;

import org.springframework.stereotype.Repository;
import pl.wlodarski.learnifier.upload.domain.Upload;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryStorage {
    private final Map<UUID, Upload> uploads = new ConcurrentHashMap<>();

    public Upload upload(Upload upload) {
        uploads.put(upload.getId(), upload);
        return upload;
    }

    public Optional<Upload> getById(UUID id) {
        return Optional.ofNullable(uploads.get(id));
    }

    public synchronized boolean remove(UUID uuid) {
        Upload remove = uploads.remove(uuid);
        return remove != null;
    }

}
