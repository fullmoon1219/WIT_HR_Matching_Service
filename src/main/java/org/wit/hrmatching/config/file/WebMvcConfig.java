package org.wit.hrmatching.config.file;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 실제 파일 시스템 경로 → URL 경로 매핑
        registry.addResourceHandler("/uploads/posts/**")
                .addResourceLocations("file:///C:/Java/upload/posts/contents/"); // ← 마지막에 / 꼭 있어야 함
    }
}
