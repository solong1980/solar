package com.lszyhb.basicclass;

/**
 * Created by kkk8199 on 12/16/17.
 */

public class ShowDevConfig {
    // project_id
    // dev_type
    // config
    private Long id;
    private Long projectId;
    private Integer devType;
    private String config;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getDevType() {
        return devType;
    }

    public void setDevType(Integer devType) {
        this.devType = devType;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

}
