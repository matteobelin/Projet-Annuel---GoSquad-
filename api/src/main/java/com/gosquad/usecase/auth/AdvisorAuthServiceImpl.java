package com.gosquad.usecase.auth;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.advisors.AdvisorModel;
import com.gosquad.infrastructure.persistence.advisors.AdvisorRepository;
import com.gosquad.infrastructure.persistence.company.CompanyModel;
import com.gosquad.infrastructure.persistence.company.CompanyRepository;
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

    private final AdvisorRepository advisorRepository;
    private final CompanyRepository companyRepository;
    private final Key key;

    @Autowired
    public AdvisorAuthServiceImpl(AdvisorRepository advisorRepository, CompanyRepository companyRepository) {
        this.advisorRepository = advisorRepository;
        this.companyRepository = companyRepository;

        String secret = Dotenv.load().get("JWT_SECRET");
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public AdvisorAuthServiceImpl(AdvisorRepository advisorRepository, CompanyRepository companyRepository, String jwtSecret) {
        this.advisorRepository = advisorRepository;
        this.companyRepository = companyRepository;
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public boolean authentification(String email, String password, String companyCode)
            throws SQLException, NotFoundException, ConstraintViolationException {
        AdvisorModel advisorModel = advisorRepository.getByEmail(email);
        CompanyModel companyModel = companyRepository.getById(advisorModel.getCompagnyId());

        return BCrypt.checkpw(password, advisorModel.getPassword()) && companyModel.getCode().equals(companyCode);
    }


    public String generateJwtToken(String email, String companyCode)
            throws SQLException, ConstraintViolationException, NotFoundException {

        AdvisorModel advisorModel = advisorRepository.getByEmail(email);
        CompanyModel companyModel = companyRepository.getById(advisorModel.getCompagnyId());

        if (!companyModel.getCode().equals(companyCode)) {
            throw new ConstraintViolationException("Company code incorrect");
        }

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 3600000;
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        String jwt = Jwts.builder()
                .setSubject(String.valueOf(advisorModel.getId()))
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim("role", advisorModel.getRole())
                .claim("companyCode", companyCode)
                .signWith(key)
                .compact();

        return jwt;
    }
}
