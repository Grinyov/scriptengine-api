package com.grinyov.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.Identifiable;
import javax.persistence.*;
import javax.script.CompiledScript;

/**
 * @author vgrinyov
 */
@Entity
// TODO(processed) Do Lombok generated hashcode and equals implementations match requirements of JPA entities?
// that's one of reasons I'd not recommend using Lombok. Another reason is that it does not play well with other annotation processors, like aspectj
// https://docs.jboss.org/hibernate/stable/core.old/reference/en/html/persistent-classes-equalshashcode.html
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

    private String result;

    public Script() {
    }

    public Long getId() {
        return this.id;
    }

    public String getScript() {
        return this.script;
    }

    public Status getStatus() {
        return this.status;
    }

    public String getResult() {
        return this.result;
    }

    public CompiledScript getCompiledScript() {
        return this.compiledScript;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setCompiledScript(CompiledScript compiledScript) {
        this.compiledScript = compiledScript;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Script)) return false;
        final Script other = (Script) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$script = this.getScript();
        final Object other$script = other.getScript();
        if (this$script == null ? other$script != null : !this$script.equals(other$script)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$result = this.getResult();
        final Object other$result = other.getResult();
        if (this$result == null ? other$result != null : !this$result.equals(other$result)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $script = this.getScript();
        result = result * PRIME + ($script == null ? 43 : $script.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $result = this.getResult();
        result = result * PRIME + ($result == null ? 43 : $result.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Script;
    }

    public String toString() {
        return "com.grinyov.model.Script(id=" + this.getId() + ", script=" + this.getScript() + ", status=" + this.getStatus() + ", result=" + this.getResult() + ")";
    }

    @Transient
    @JsonIgnore
    private CompiledScript compiledScript;
}
