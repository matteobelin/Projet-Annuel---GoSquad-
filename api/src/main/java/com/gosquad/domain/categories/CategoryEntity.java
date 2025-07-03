package com.gosquad.domain.categories;

import com.gosquad.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryEntity extends Entity {
    String name;

    public CategoryEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }
}


