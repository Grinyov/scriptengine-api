package com.grinyov.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;

/**
 *  Application additional configuration
 *
 *  @author vgrinyov
 */
@Configuration
@EnableCaching
public class AppConfig {

    @Bean
    public CurieProvider curieProvider() {
        return new DefaultCurieProvider("/", new UriTemplate("http://localhost:8080/asciidoc/api-guide.html#{rel}"));
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("scripts");
    }
}
