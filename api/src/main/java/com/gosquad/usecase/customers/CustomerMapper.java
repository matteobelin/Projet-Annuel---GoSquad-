package com.gosquad.usecase.customers;

import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.infrastructure.persistence.customers.CustomerModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {

    public CustomerEntity modelToEntity(CustomerModel customerModel) {
        return new CustomerEntity(
                customerModel.getId(),
                customerModel.getFirstname(),
                customerModel.getLastname(),
                customerModel.getEmail(),
                customerModel.getPhoneNumber(),
                customerModel.getBirthDate(),
                customerModel.getIdCardNumber(),
                customerModel.getIdCardExpirationDate(),
                customerModel.getIdCardCopyUrl(),
                customerModel.getPassportNumber(),
                customerModel.getPassportExpirationDate(),
                customerModel.getPassportCopyUrl(),
                customerModel.getCountryId(),
                customerModel.getAddressId(),
                customerModel.getBillingAddressId(),
                customerModel.getCompanyId(),
                customerModel.getCustomer_number()
        );


    }
    public CustomerModel entityToModel(CustomerEntity customerEntity){
        return new CustomerModel(
                customerEntity.getId(),
                customerEntity.getFirstname(),
                customerEntity.getLastname(),
                customerEntity.getEmail(),
                customerEntity.getPhoneNumber(),
                customerEntity.getBirthDate(),
                customerEntity.getIdCardNumber(),
                customerEntity.getIdCardExpirationDate(),
                customerEntity.getIdCardCopyUrl(),
                customerEntity.getPassportNumber(),
                customerEntity.getPassportExpirationDate(),
                customerEntity.getPassportCopyUrl(),
                customerEntity.getCountryId(),
                customerEntity.getAddressId(),
                customerEntity.getBillingAddressId(),
                customerEntity.getCompanyId(),
                customerEntity.getCustomer_number()
        );
    }

    public List<CustomerEntity> modelToEntity(List<CustomerModel> customerModels){;
        return customerModels.stream()
                .map(this::modelToEntity)
                .toList();
    };
}
