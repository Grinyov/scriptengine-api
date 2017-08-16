package com.grinyov.service;

import com.grinyov.model.Script;

/**
 * The service for starting, detailing of script and finishing the script processing
 *
 * @author vgrinyov
 */

public interface ScriptProccessingService {

    /**
     * Starts processing of specified script
     *
     * @param id 
     * @return script
     */
    String perform(Long id);

    /**
     * Shows status of script
     *
     * @param id
     * @return script
     */
    Script status(Long id);

    /**
     * Interrupts the processing of this script
     *
     * @param id
     * @return script
     */
    Script terminate(Long id);

    /**
     * Views the body of script
     *
     * @param id
     * @return script's body as plain text
     */
    String viewBody(Long id);

    /**
     * Views the detail of running the script
     *
     * @param id
     * @return result of running the script as plain text
     */
    String viewResult(Long id);

}
