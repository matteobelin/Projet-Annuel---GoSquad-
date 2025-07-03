package com.gosquad.infrastructure.persistence.activities;

import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityModel extends Model {
    private String name;
    private String description;
    private Integer addressId;
    private Integer priceId;
    private Integer categoryId;

    public ActivityModel(Integer id, String name, String description, Integer addressId, Integer priceId, Integer categoryId) {
        super(id);
        this.name = name;
        this.description = description;
        this.addressId = addressId;
        this.priceId = priceId;
        this.categoryId = categoryId;
    }
}

