package com.gosquad.infrastructure.persistence;
import lombok.Data;

@Data
public abstract class Model {
    private Integer id;

    public Model() {}
    protected Model(Integer id) {
        this.id = id;
    }
}