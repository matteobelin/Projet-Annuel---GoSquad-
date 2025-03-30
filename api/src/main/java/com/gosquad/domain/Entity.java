package com.gosquad.domain;

import lombok.Data;

@Data
public abstract class Entity {
    private int id;

    public Entity() {}

    protected Entity(int id) {
        this.id = id;
    }
}
