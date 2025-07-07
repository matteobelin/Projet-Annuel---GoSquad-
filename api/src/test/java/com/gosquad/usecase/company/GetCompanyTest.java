package com.gosquad.usecase.company;

import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.usecase.company.utils.GetCompany;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetCompanyTest {

    private CompanyService companyService;
    private JWTInterceptor jwtInterceptor;
    private GetCompany getCompany;

    private HttpServletRequest request;

    @BeforeEach
    void setup() {
        companyService = mock(CompanyService.class);
        jwtInterceptor = mock(JWTInterceptor.class);
        getCompany = new GetCompany(companyService, jwtInterceptor);

        request = mock(HttpServletRequest.class);
    }

    @Test
    void testGetCompanyFromRequest() throws Exception {
        String fakeToken = "fake.jwt.token";
        String authHeader = "Bearer " + fakeToken;

        when(request.getHeader("Authorization")).thenReturn(authHeader);

        Map<String, Object> tokenInfo = Map.of("companyCode", "ABC123");
        when(jwtInterceptor.extractTokenInfo(fakeToken)).thenReturn(tokenInfo);

        CompanyEntity expectedCompany = new CompanyEntity(
                1, "ABC", "Test Company"
        );

        when(companyService.getCompanyByCode("ABC123")).thenReturn(expectedCompany);

        CompanyEntity result = getCompany.getCompanyFromRequest(request);

        assertNotNull(result);
        assertEquals("ABC", result.getCode());

        verify(request).getHeader("Authorization");
        verify(jwtInterceptor).extractTokenInfo(fakeToken);
        verify(companyService).getCompanyByCode("ABC123");
    }

    @Test
    void testGetCustomerIdByCompany() {
        CompanyEntity company = new CompanyEntity(
                1,"COMP","test"
        );

        String customerId = "COMP12345";

        int result = getCompany.GetCustomerIdByCompany(customerId, company);

        assertEquals(12345, result);
    }
}
