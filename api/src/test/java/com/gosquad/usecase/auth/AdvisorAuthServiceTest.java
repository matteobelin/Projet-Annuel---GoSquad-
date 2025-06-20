package com.gosquad.usecase.auth;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.advisors.AdvisorEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.usecase.advisors.AdvisorService;
import com.gosquad.usecase.auth.impl.AdvisorAuthServiceImpl;
import com.gosquad.usecase.company.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AdvisorAuthServiceTest {

    @Mock
    private AdvisorService advisorService;

    @Mock
    private CompanyService companyService;

    private AdvisorAuthServiceImpl authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        String fakeSecret = "12345678901234567890123456789012"; // 32 chars = 256 bits
        authService = new AdvisorAuthServiceImpl(advisorService, companyService, fakeSecret);
    }

    @Test
    public void testAuthentification_Success() throws SQLException, NotFoundException, ConstraintViolationException {
        // Arrange
        String email = "test@example.com";
        String rawPassword = "password123";
        String companyCode = "COMP123";

        AdvisorEntity advisorEntity = new AdvisorEntity(1, "John", "Doe", email, "123456789", "ROLE_ADVISOR", 1);
        CompanyEntity companyEntity = new CompanyEntity(1, companyCode, "GoSquad Inc.");

        when(advisorService.getAdvisorByEmail(email)).thenReturn(advisorEntity);
        when(advisorService.validateAdvisorPassword(email, rawPassword)).thenReturn(true);
        when(companyService.getCompanyById(1)).thenReturn(companyEntity);

        // Act
        boolean result = authService.authentification(email, rawPassword, companyCode);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAuthentification_Fail_WrongPassword() throws SQLException, NotFoundException, ConstraintViolationException {
        // Arrange
        String email = "test@example.com";
        String wrongPassword = "wrongpassword";
        String companyCode = "COMP123";

        AdvisorEntity advisorEntity = new AdvisorEntity(1, "John", "Doe", email, "123456789", "ROLE_ADVISOR", 1);
        CompanyEntity companyEntity = new CompanyEntity(1, companyCode, "GoSquad Inc.");

        when(advisorService.getAdvisorByEmail(email)).thenReturn(advisorEntity);
        when(advisorService.validateAdvisorPassword(email, wrongPassword)).thenReturn(false);
        when(companyService.getCompanyById(1)).thenReturn(companyEntity);

        // Act
        boolean result = authService.authentification(email, wrongPassword, companyCode);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAuthentification_Fail_WrongCompanyCode() throws SQLException, NotFoundException, ConstraintViolationException {
        // Arrange
        String email = "test@example.com";
        String rawPassword = "password123";
        String companyCode = "COMP123";
        String wrongCompanyCode = "WRONG";

        AdvisorEntity advisorEntity = new AdvisorEntity(1, "John", "Doe", email, "123456789", "ROLE_ADVISOR", 1);
        CompanyEntity companyEntity = new CompanyEntity(1, companyCode, "GoSquad Inc.");

        when(advisorService.getAdvisorByEmail(email)).thenReturn(advisorEntity);
        when(advisorService.validateAdvisorPassword(email, rawPassword)).thenReturn(true);
        when(companyService.getCompanyById(1)).thenReturn(companyEntity);

        // Act
        boolean result = authService.authentification(email, rawPassword, wrongCompanyCode);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGenerateJwtToken_Success() throws SQLException, ConstraintViolationException, NotFoundException {
        // Arrange
        String email = "test@example.com";
        String companyCode = "COMP123";

        AdvisorEntity advisorEntity = new AdvisorEntity(42, "John", "Doe", email, "123456789", "ROLE_ADVISOR", 1);
        CompanyEntity companyEntity = new CompanyEntity(1, companyCode, "GoSquad Inc.");

        when(advisorService.getAdvisorByEmail(email)).thenReturn(advisorEntity);
        when(companyService.getCompanyById(1)).thenReturn(companyEntity);

        // Act
        String jwt = authService.generateJwtToken(email, companyCode);

        // Assert
        assertNotNull(jwt);
        assertTrue(jwt.length() > 0);
        // Optionnel: vérifier que le JWT contient les bonnes claims
        assertTrue(jwt.contains("."));
    }

    @Test
    public void testGenerateJwtToken_Fail_WrongCompanyCode() throws SQLException, NotFoundException, ConstraintViolationException {
        // Arrange
        String email = "test@example.com";
        String companyCode = "COMP123";
        String wrongCompanyCode = "WRONG";

        AdvisorEntity advisorEntity = new AdvisorEntity(42, "John", "Doe", email, "123456789", "ROLE_ADVISOR", 1);
        CompanyEntity companyEntity = new CompanyEntity(1, companyCode, "GoSquad Inc.");

        when(advisorService.getAdvisorByEmail(email)).thenReturn(advisorEntity);
        when(companyService.getCompanyById(1)).thenReturn(companyEntity);

        // Act & Assert
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            authService.generateJwtToken(email, wrongCompanyCode);
        });

        assertEquals("Company code incorrect", exception.getMessage());
    }

    @Test
    public void testAuthentification_Fail_PasswordValidationFalse() throws SQLException, NotFoundException, ConstraintViolationException {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String companyCode = "COMP123";

        AdvisorEntity advisorEntity = new AdvisorEntity(1, "John", "Doe", email, "123456789", "ROLE_ADVISOR", 1);
        CompanyEntity companyEntity = new CompanyEntity(1, companyCode, "GoSquad Inc.");

        when(advisorService.getAdvisorByEmail(email)).thenReturn(advisorEntity);
        when(advisorService.validateAdvisorPassword(email, password)).thenReturn(false);
        when(companyService.getCompanyById(1)).thenReturn(companyEntity);

        // Act
        boolean result = authService.authentification(email, password, companyCode);

        // Assert
        assertFalse(result);
    }
}