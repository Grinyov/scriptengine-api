package com.grinyov.config;

import com.grinyov.util.ScriptValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;

/**
 *  Application additional configuration
 *
 *  @author vgrinyov
 */
@Configuration
public class AppConfig {

    @Configuration
    public static class CustomRepositoryRestMvcConfiguration extends RepositoryRestConfigurerAdapter {
        @Override
        public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
            validatingListener.addValidator("beforeCreate", new ScriptValidator());
        }
    }

    @Bean
    public CurieProvider curieProvider() {
        return new DefaultCurieProvider("/", new UriTemplate("http://localhost:8080/asciidoc/api-guide.html#{rel}"));
    }

}
