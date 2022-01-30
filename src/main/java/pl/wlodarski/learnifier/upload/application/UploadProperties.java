package pl.wlodarski.learnifier.upload.application;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Arrays;
import java.util.List;

@Value
@ConstructorBinding
@ConfigurationProperties("upload")
public class UploadProperties {
    String types;

    public List<String> getSupportedTypesAsList() {
        return Arrays.stream(types.split(",")).toList();
    }
}
