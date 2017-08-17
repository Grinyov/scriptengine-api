package com.grinyov.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by vgrinyov on 17.08.2017.
 */
public class ScriptLaunched extends ApplicationEvent {

    Long id;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ScriptLaunched(Object source, Long id) {
        super(source);
        this.id = id;
    }
}
