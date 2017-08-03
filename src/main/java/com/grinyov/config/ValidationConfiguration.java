package com.grinyov.config;

import com.grinyov.service.util.ScriptValidator;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 *  Enable validator for check compilation of scripts
 *
 * @author vgrinyov
 */
@Configuration
public class ValidationConfiguration {
    @Configuration
    public static class CustomRepositoryRestMvcConfiguration extends RepositoryRestConfigurerAdapter {
        @Override
        public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
            validatingListener.addValidator("beforeCreate", new ScriptValidator());
        }
    }
}
