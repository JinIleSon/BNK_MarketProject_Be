package kr.co.bnk_marketproject_be.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 실제 절대경로 (예: /Users/jeonsehyeon/Desktop/BNK/MarketProject_Be/upload/)
        String absPath = "file:" + Paths.get("upload").toAbsolutePath().toString() + "/";

        registry.addResourceHandler(
                "/upload/**",          // 일반 경로 매핑
                "/NICHIYA/upload/**"   // context-path 포함 경로 매핑
        ).addResourceLocations(absPath);

    }
}
