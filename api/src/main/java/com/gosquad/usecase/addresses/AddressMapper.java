package com.gosquad.usecase.addresses;

import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.infrastructure.persistence.addresses.AddressModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressMapper {
    public AddressEntity modelToEntity(AddressModel address) {
        return new AddressEntity(
                address.getId(),
                address.getAddressLine(),
                address.getCityId()
        );
    }

    public AddressModel entityToModel(AddressEntity address) {
        return new AddressModel(
                address.getId(),
                address.getAddressLine(),
                address.getCityId()
        );
    }

    public List<AddressEntity> modelsToEntities(List<AddressModel> addressModels) {
        return addressModels.stream()
                .map(this::modelToEntity)
                .toList();
    }
}
