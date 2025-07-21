package com.gosquad.usecase.addresses.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.addresses.AddressEntity;
import com.gosquad.infrastructure.persistence.addresses.AddressModel;
import com.gosquad.infrastructure.persistence.addresses.AddressRepository;
import com.gosquad.usecase.addresses.AddressMapper;
import com.gosquad.usecase.addresses.AddressService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = new AddressMapper();
    }

    public AddressEntity getAddressByID(int id) throws SQLException, NotFoundException {
        AddressModel addressModel = addressRepository.getById(id);
        return addressMapper.modelToEntity(addressModel);
    }

    public AddressEntity getAddressByAddressLineByCityId(String addressLine, int cityId) throws SQLException, NotFoundException, ConstraintViolationException {
        AddressModel addressModel = addressRepository.getByAddressLineByCityId(addressLine, cityId);
        return addressMapper.modelToEntity(addressModel);
    }

    public List<AddressEntity> findByIds(List<Integer> ids) throws SQLException{
        List<AddressModel> addressModels = addressRepository.findByIds(ids);
        return addressMapper.modelsToEntities(addressModels);
    }

    public void addAddress(AddressEntity address) throws SQLException{
        AddressModel addressModel = addressMapper.entityToModel(address);
        addressRepository.addAddress(addressModel);
        address.setId(addressModel.getId());
    };

    public void updateAddress(AddressEntity address) throws SQLException, ConstraintViolationException{
        AddressModel addressModel = addressMapper.entityToModel(address);
        addressRepository.updateAddress(addressModel);
    };

    public AddressEntity getOrCreateAddress(String addressLine, Integer cityId) throws SQLException, NotFoundException, ConstraintViolationException {
        try {
            return getAddressByAddressLineByCityId(addressLine, cityId);
        } catch (NotFoundException | ConstraintViolationException e) {
            AddressEntity newAddress = new AddressEntity(null, addressLine, cityId);
            addAddress(newAddress);
            return getAddressByAddressLineByCityId(addressLine, cityId);
        }
    }
}
