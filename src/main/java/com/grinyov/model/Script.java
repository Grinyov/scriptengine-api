package com.grinyov.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by vgrinyov.
 */
@Entity
@Data
public class Script {
    @Id
    @GeneratedValue
    private Long id;
    private String script;
    private String status;
    private String result;
}
