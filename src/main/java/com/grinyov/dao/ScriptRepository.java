package com.grinyov.dao;

import com.grinyov.model.Script;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by vgrinyov.
 */
public interface ScriptRepository extends PagingAndSortingRepository<Script, Long> {
}
