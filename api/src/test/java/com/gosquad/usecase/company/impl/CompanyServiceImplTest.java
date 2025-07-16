package com.gosquad.usecase.company.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.infrastructure.persistence.company.CompanyModel;
import com.gosquad.infrastructure.persistence.company.CompanyRepository;
import com.gosquad.usecase.company.CompanyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyServiceImplTest {
    private CompanyRepository companyRepository;
    private CompanyMapper companyMapper;
    private CompanyServiceImpl companyService;

    @BeforeEach
    void setUp() {
        companyRepository = mock(CompanyRepository.class);
        companyMapper = new CompanyMapper();
        companyService = new CompanyServiceImpl(companyRepository, companyMapper);
    }

    @Test
    void testGetCompanyByIdSuccess() throws SQLException, NotFoundException {
        CompanyModel model = new CompanyModel(1, "CODE1", "Company One");
        when(companyRepository.getById(1)).thenReturn(model);
        CompanyEntity entity = companyService.getCompanyById(1);
        assertNotNull(entity);
        assertEquals(1, entity.getId());
        assertEquals("CODE1", entity.getCode());
        assertEquals("Company One", entity.getName());
    }

    @Test
    void testGetCompanyByIdNotFound() throws SQLException, NotFoundException {
        when(companyRepository.getById(99)).thenThrow(new NotFoundException("Not found"));
        assertThrows(NotFoundException.class, () -> companyService.getCompanyById(99));
    }

    @Test
    void testGetCompanyByCodeSuccess() throws SQLException, NotFoundException {
        CompanyModel model = new CompanyModel(2, "CODE2", "Company Two");
        when(companyRepository.getByCode("CODE2")).thenReturn(model);
        CompanyEntity entity = companyService.getCompanyByCode("CODE2");
        assertNotNull(entity);
        assertEquals(2, entity.getId());
        assertEquals("CODE2", entity.getCode());
        assertEquals("Company Two", entity.getName());
    }

    @Test
    void testGetCompanyByCodeNotFound() throws SQLException, NotFoundException {
        when(companyRepository.getByCode("UNKNOWN")).thenThrow(new NotFoundException("Not found"));
        assertThrows(NotFoundException.class, () -> companyService.getCompanyByCode("UNKNOWN"));
    }
}
