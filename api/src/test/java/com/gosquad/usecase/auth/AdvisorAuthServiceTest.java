package com.gosquad.usecase.auth;
import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.advisors.AdvisorModel;
import com.gosquad.infrastructure.persistence.advisors.AdvisorRepository;
import com.gosquad.infrastructure.persistence.company.CompanyModel;
import com.gosquad.infrastructure.persistence.company.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AdvisorAuthServiceTest {
    @Mock
    private AdvisorRepository advisorRepository;

    @Mock
    private CompanyRepository companyRepository;

    private AdvisorAuthServiceImpl authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        String fakeSecret = "12345678901234567890123456789012"; // 32 chars = 256 bits
        authService = new AdvisorAuthServiceImpl(advisorRepository, companyRepository, fakeSecret);
    }

    @Test
    public void testAuthentification_Success() throws SQLException, NotFoundException, ConstraintViolationException {
        // Arrange
        String email = "test@example.com";
        String rawPassword = "password123";
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        String companyCode = "COMP123";

        AdvisorModel advisor = new AdvisorModel(1, email, hashedPassword, 1, "ROLE_ADVISOR");
        advisor.setPassword(hashedPassword);
        advisor.setCompagnyId(1);

        CompanyModel company = new CompanyModel(1, companyCode, "GoSquad Inc.");;
        company.setCode(companyCode);

        when(advisorRepository.getByEmail(email)).thenReturn(advisor);
        when(companyRepository.getById(1)).thenReturn(company);

        // Act
        boolean result = authService.authentification(email, rawPassword, companyCode);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAuthentification_Fail_WrongPassword() throws SQLException, NotFoundException, ConstraintViolationException {
        // Arrange
        String email = "test@example.com";
        String rawPassword = "wrongpassword";
        String hashedPassword = BCrypt.hashpw("correctpassword", BCrypt.gensalt());
        String companyCode = "COMP123";

        AdvisorModel advisor = new AdvisorModel(1, email, hashedPassword, 1, "ROLE_ADVISOR");
        advisor.setPassword(hashedPassword);
        advisor.setCompagnyId(1);

        CompanyModel company = new CompanyModel(1, companyCode, "GoSquad Inc.");;
        company.setCode(companyCode);

        when(advisorRepository.getByEmail(email)).thenReturn(advisor);
        when(companyRepository.getById(1)).thenReturn(company);

        // Act
        boolean result = authService.authentification(email, rawPassword, companyCode);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAuthentification_Fail_WrongCompanyCode() throws SQLException, NotFoundException, ConstraintViolationException {
        // Arrange
        String email = "test@example.com";
        String rawPassword = "password123";
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        String companyCode = "COMP123";
        String wrongCompanyCode = "WRONG";

        AdvisorModel advisor = new AdvisorModel(1, email, hashedPassword, 1, "ROLE_ADVISOR");
        advisor.setPassword(hashedPassword);
        advisor.setCompagnyId(1);

        CompanyModel company = new CompanyModel(1, companyCode, "GoSquad Inc.");;
        company.setCode(companyCode);

        when(advisorRepository.getByEmail(email)).thenReturn(advisor);
        when(companyRepository.getById(1)).thenReturn(company);

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

        AdvisorModel advisor = new AdvisorModel(1, email, "hashedPassword", 1, "ROLE_ADVISOR");
        advisor.setId(42);
        advisor.setRole("ROLE_ADVISOR");
        advisor.setCompagnyId(1);

        CompanyModel company = new CompanyModel(1, companyCode, "GoSquad Inc.");;
        company.setCode(companyCode);

        when(advisorRepository.getByEmail(email)).thenReturn(advisor);
        when(companyRepository.getById(1)).thenReturn(company);

        // Act
        String jwt = authService.generateJwtToken(email, companyCode);

        // Assert
        assertNotNull(jwt);
        assertTrue(jwt.length() > 0);
    }

    @Test
    public void testGenerateJwtToken_Fail_WrongCompanyCode() throws SQLException, NotFoundException, ConstraintViolationException {
        // Arrange
        String email = "test@example.com";
        String companyCode = "COMP123";
        String wrongCompanyCode = "WRONG";

        AdvisorModel advisor = new AdvisorModel(1, email, "hashedPassword", 1, "ROLE_ADVISOR");
        advisor.setId(42);
        advisor.setRole("ROLE_ADVISOR");
        advisor.setCompagnyId(1);

        CompanyModel company = new CompanyModel(1, companyCode, "GoSquad Inc.");;
        company.setCode(companyCode);

        when(advisorRepository.getByEmail(email)).thenReturn(advisor);
        when(companyRepository.getById(1)).thenReturn(company);

        // Act & Assert
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            authService.generateJwtToken(email, wrongCompanyCode);
        });

        assertEquals("Company code incorrect", exception.getMessage());
    }

}
