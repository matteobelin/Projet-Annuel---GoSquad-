package com.gosquad.domain.activities;

import com.gosquad.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityEntity extends Entity {
        private String name;
        private String description;
        private Integer addressId;
        private Integer priceId;
        private Integer categoryId;
        private Integer companyId;

        public ActivityEntity(Integer id, String name, String description, Integer addressId, Integer priceId, Integer categoryId, Integer companyId) {
            super(id);
            this.name = name;
            this.description = description;
            this.addressId = addressId;
            this.priceId = priceId;
            this.categoryId = categoryId;
            this.companyId = companyId;
        }
    }

