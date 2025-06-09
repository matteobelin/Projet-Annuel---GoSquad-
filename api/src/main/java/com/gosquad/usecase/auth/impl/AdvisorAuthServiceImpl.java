package com.gosquad.usecase.auth.impl;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.advisors.AdvisorEntity;
import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.infrastructure.persistence.advisors.AdvisorModel;
import com.gosquad.infrastructure.persistence.advisors.AdvisorRepository;
import com.gosquad.infrastructure.persistence.company.CompanyModel;
import com.gosquad.infrastructure.persistence.company.CompanyRepository;
import com.gosquad.usecase.advisors.AdvisorService;
import com.gosquad.usecase.auth.AdvisorAuthService;
import com.gosquad.usecase.company.CompanyService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

import java.security.Key;
import java.sql.SQLException;
import java.util.Date;

@Service
public class AdvisorAuthServiceImpl implements AdvisorAuthService {

    private final AdvisorService advisorService;
    private final CompanyService companyService;
    private final Key key;

    @Autowired
    public AdvisorAuthServiceImpl(AdvisorService advisorService, CompanyService companyService) {
        this.advisorService = advisorService;
        this.companyService = companyService;

        String secret = Dotenv.load().get("JWT_SECRET");
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public AdvisorAuthServiceImpl(AdvisorService advisorService, CompanyService companyService, String jwtSecret) {
        this.advisorService = advisorService;
        this.companyService = companyService;
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public boolean authentification(String email, String password, String companyCode)
            throws SQLException, NotFoundException, ConstraintViolationException {
        AdvisorEntity advisorEntity = advisorService.getAdvisorByEmail(email);
        CompanyEntity companyEntity = companyService.getCompanyById(advisorEntity.getCompanyId());

        return advisorService.validateAdvisorPassword(email,password) && companyEntity.getCode().equals(companyCode);
    }


    public String generateJwtToken(String email, String companyCode)
            throws SQLException, ConstraintViolationException, NotFoundException {

        AdvisorEntity advisorEntity = advisorService.getAdvisorByEmail(email);
        CompanyEntity companyEntity = companyService.getCompanyById(advisorEntity.getCompanyId());

        if (!companyEntity.getCode().equals(companyCode)) {
            throw new ConstraintViolationException("Company code incorrect");
        }

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 12 * 60 * 60 * 1000;;
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        String jwt = Jwts.builder()
                .setSubject(String.valueOf(advisorEntity.getId()))
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim("role", advisorEntity.getRole())
                .claim("companyCode", companyCode)
                .signWith(key)
                .compact();

        return jwt;
    }
}
