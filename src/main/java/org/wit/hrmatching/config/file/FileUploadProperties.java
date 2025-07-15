package org.wit.hrmatching.config.file;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "upload")
@Data
public class FileUploadProperties {
    private String postDir;
    private String resumeDir;
    private String jobDir;
    private String postImageDir;
}

