package com.grinyov.service.util;

import com.grinyov.domain.Script;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.script.*;

/**
 * Check that the added scripts are compiled
 * and saved compiled script to field compiledScript
 *
 * @author vgrinyov
 */
@Component
public class ScriptValidator {
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

    public boolean validate(Script target) {
        final Script script = (Script) target;
        ScriptEngine engine = getEngine();

        if (!compileScript(script, engine)) {
            logger.error("The script can not compile");
            return false;
        }
        return true;
    }
}
