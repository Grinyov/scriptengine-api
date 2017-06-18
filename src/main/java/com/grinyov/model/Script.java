package com.grinyov.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by vgrinyov.
 */
@Entity
@Data
public class Script {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String script;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    public enum Status {

        NEW,

        RUNNING,

        DONE,

        FAILED
    }

    @Column(nullable = false)
    private String result;
}
