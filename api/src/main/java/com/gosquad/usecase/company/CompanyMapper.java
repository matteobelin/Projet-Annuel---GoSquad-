package com.gosquad.usecase.company;

import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.infrastructure.persistence.company.CompanyModel;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public CompanyEntity modelToEntity(CompanyModel companyModel) {
        return new CompanyEntity(
                companyModel.getId(),
                companyModel.getCode(),
                companyModel.getName()
        );
    }
}
