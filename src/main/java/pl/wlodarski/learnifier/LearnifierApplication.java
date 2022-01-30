package pl.wlodarski.learnifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.wlodarski.learnifier.upload.application.UploadProperties;

@SpringBootApplication
@EnableConfigurationProperties({UploadProperties.class})
public class LearnifierApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnifierApplication.class, args);
    }

}
