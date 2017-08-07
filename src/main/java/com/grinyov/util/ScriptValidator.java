package com.grinyov.util;

import com.grinyov.model.Script;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.script.*;

/**
 * TODO since valid script body is an invariant of Script object, consider moving this functionality into Script object. Read about DDD and invariants. 
 * 
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
            CompiledScript compiledScript = ((Compilable) engine).compile(script.getBody());
            logger.debug("Script compiled successful. :-) \n");
            script.setCompiledScript(compiledScript);
            return true;
        } catch (ScriptException e) {
            // TODO important information from ScriptException is lost!!!
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
            // TODO returned error object does not contain important information about error
            errors.rejectValue("script", "Script.script.compile.failed", "The script did not compile");
        }
    }
}
