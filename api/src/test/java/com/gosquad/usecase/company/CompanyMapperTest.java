package com.gosquad.usecase.company;

import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.infrastructure.persistence.company.CompanyModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CompanyMapperTest {

    private final CompanyMapper companyMapper = new CompanyMapper();

    @Test
    void modelToEntity_should_map_fields_correctly() {
        // arrange
        CompanyModel model = new CompanyModel(1,"CODE123","My Company");

        // act
        CompanyEntity entity = companyMapper.modelToEntity(model);

        // assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getCode()).isEqualTo("CODE123");
        assertThat(entity.getName()).isEqualTo("My Company");
    }
}
