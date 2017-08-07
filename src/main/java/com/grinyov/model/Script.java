package com.grinyov.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.script.CompiledScript;

/**
 * @author vgrinyov
 */
@Entity
// TODO Do Lombok generated hashcode and equals implementations match requirements of JPA entities?
// that's one of reasons I'd not recommend using Lombok. Another reason is that it does not play well with other annotation processors, like aspectj
// https://docs.jboss.org/hibernate/stable/core.old/reference/en/html/persistent-classes-equalshashcode.html
@Data
public class Script implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

   /* // for http caching
    @Version
    private Integer version;*/

    // TODO consider using  @Basic(fetch = FetchType.LAZY) for script body and result (output) properties
    
    @Column(nullable = false)
    private String script;

    // TODO We need script timings in the script resource json representation - when it was created, when it was executed, how long time it took, when it was completed or terminated
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    public enum Status {

        NEW,

        RUNNING,

        DONE,

        FAILED
    }

    private String result;

    @Transient
    @JsonIgnore
    private CompiledScript compiledScript;
}
