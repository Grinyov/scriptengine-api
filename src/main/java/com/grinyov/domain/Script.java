package com.grinyov.domain;


import javax.persistence.*;
import javax.script.CompiledScript;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grinyov.domain.enumeration.Status;

/**
 * A Script.
 */
@Entity
@Table(name = "script")
public class Script implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "script", nullable = false)
    private String script;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "result")
    private String result;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScript() {
        return script;
    }

    public Script script(String script) {
        this.script = script;
        return this;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Status getStatus() {
        return status;
    }

    public Script status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public Script result(String result) {
        this.result = result;
        return this;
    }

    @Transient
    @JsonIgnore
    private CompiledScript compiledScript;

    public CompiledScript getCompiledScript() {
        return compiledScript;
    }

    public void setCompiledScript(CompiledScript compiledScript) {
        this.compiledScript = compiledScript;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public Script user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Script script = (Script) o;
        if (script.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), script.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Script{" +
            "id=" + getId() +
            ", script='" + getScript() + "'" +
            ", status='" + getStatus() + "'" +
            ", result='" + getResult() + "'" +
            "}";
    }
}
