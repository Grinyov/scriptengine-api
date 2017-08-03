package com.grinyov.repository;

import com.grinyov.domain.Script;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Script entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScriptRepository extends JpaRepository<Script,Long> {

    @Query("select script from Script script where script.user.login = ?#{principal.username}")
    List<Script> findByUserIsCurrentUser();
    
}
