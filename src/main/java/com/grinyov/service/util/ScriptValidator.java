package com.grinyov.service.util;

import com.grinyov.domain.Script;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.script.*;

/**
 * Check that the added scripts are compiled
 * and saved compiled script to field compiledScript
 *
 * @author vgrinyov
 */
public class ScriptValidator implements Validator {
    private static final Logger logger = Logger.getLogger(ScriptValidator.class);

    private ScriptEngine getEngine() {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("nashorn");
        logger.debug("Engine was created");
        return engine;
    }

    private boolean compileScript(Script script, ScriptEngine engine) {
        try {
            CompiledScript compiledScript = ((Compilable) engine).compile(script.getScript());
            logger.debug("Script compiled successful. :-) \n");
            script.setCompiledScript(compiledScript);
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

        if (!compileScript(script, engine)) {
            logger.error("The script can not compile");
            errors.rejectValue("script", "Script.script.compile.failed", "The script did not compile");
        }
    }
}
