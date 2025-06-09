package com.gosquad.infrastructure.persistence.addresses;

import com.gosquad.infrastructure.persistence.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddressModel extends Model {
    String addressLine;
    Integer cityId;
    Integer countryId;

    public AddressModel(Integer id,String addressLine,Integer cityId, Integer countryId) {
        super(id);
        this.addressLine = addressLine;
        this.cityId = cityId;
        this.countryId = countryId;
    }
}
