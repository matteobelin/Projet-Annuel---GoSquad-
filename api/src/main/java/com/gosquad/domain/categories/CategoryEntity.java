package com.gosquad.domain.categories;

import com.gosquad.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryEntity extends Entity {
    String name;
    Integer companyId;

    public CategoryEntity(Integer id, String name, Integer companyId) {
        super(id);
        this.name = name;
        this.companyId = companyId;
    }
}


