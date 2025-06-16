package com.gosquad.domain.addresses;

import com.gosquad.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddressEntity extends Entity {
    String addressLine;
    Integer cityId;

    public AddressEntity(Integer id,String addressLine,Integer cityId) {
        super(id);
        this.addressLine = addressLine;
        this.cityId = cityId;
    }
}