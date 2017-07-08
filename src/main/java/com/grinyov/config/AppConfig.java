package com.grinyov.config;

import com.grinyov.util.ScriptValidator;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 * Created by vgrinyov
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


}
