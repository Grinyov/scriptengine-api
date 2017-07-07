package com.grinyov.service;

import com.grinyov.model.Script;

/**
 * Created by vgrinyov.
 * <p>
 * The service for starting, detailing and finishing the script processing
 */

public interface ScriptProccessingService {

    /**
     * Starts processing of specified script
     *
     * @param id
     * @return script
     */
    Script perform(Long id);

    /**
     * Shows status of script
     *
     * @param id
     * @return script
     */
    Script detail(Long id);

    /**
     * Interrupts the processing of this script
     *
     * @param id
     * @return script
     */
    Script terminate(Long id);

    /**
     * Interrupts the processing of this script
     *
     * @param id
     * @return script
     */
    String viewBody(Long id);

}
