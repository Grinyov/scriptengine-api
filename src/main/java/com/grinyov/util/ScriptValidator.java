package com.grinyov.util;

import com.grinyov.exception.FailedScriptCompilationException;
import com.grinyov.model.Script;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by vgrinyov
 */
public class ScriptValidator implements Validator {

    private static final Logger logger = Logger.getLogger(ScriptValidator.class);

    public ScriptEngine getEngine() {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("nashorn");
        logger.debug("Engine was created");
        return engine;
    }

    public boolean compileScript(String script, ScriptEngine engine) {
        try {
            ((Compilable) engine).compile(script);
            logger.debug("Script compiled successful. :-) \n");
            return true;
        } catch (ScriptException e) {
            logger.warn("Script \"" + script + "\" compiled unsuccessful. :-(");
            return false;
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Script.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final Script script = (Script) target;
        ScriptEngine engine = getEngine();

        if (!compileScript(script.getScript(), engine)) {
            logger.error("The script can not compile");
            errors.rejectValue("script", "Script.script.compile.failed", "The script did not compile");
        }
    }
}
