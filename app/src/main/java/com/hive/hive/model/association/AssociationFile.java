package com.hive.hive.model.association;

public class AssociationFile {
    private String name;
    private Long createdAt;

    public AssociationFile() {
    }

    //GETTERS
    public String getName() {
        return name;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    //SETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
