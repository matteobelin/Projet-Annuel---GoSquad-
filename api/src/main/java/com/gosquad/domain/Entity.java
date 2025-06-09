package com.gosquad.domain;

import lombok.Data;

@Data
public abstract class Entity {
    private Integer id;

    public Entity() {}

    protected Entity(Integer id) {
        this.id = id;
    }
}
