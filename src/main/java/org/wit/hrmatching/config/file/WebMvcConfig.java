package org.wit.hrmatching.config.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${upload.post-image-dir}")
    private String postImageDir;

    @Value("${upload.user-profile}")
    public String userProfileImgDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/posts/**")
                .addResourceLocations("file:///" + postImageDir);

        registry.addResourceHandler("/uploads/users/profile/**")
                .addResourceLocations("file:///" + userProfileImgDir);
    }
}

