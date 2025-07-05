package com.gosquad.usecase.company.utils;

import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import com.gosquad.usecase.company.CompanyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GetCompany {

    private final CompanyService companyService;
    private final JWTInterceptor jwtInterceptor;

    public GetCompany(CompanyService companyService, JWTInterceptor jwtInterceptor) {
        this.companyService = companyService;
        this.jwtInterceptor = jwtInterceptor;
    }

    public CompanyEntity getCompanyFromRequest(HttpServletRequest request) throws Exception {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);

        String companyCode = tokenInfo.get("companyCode").toString();
        return companyService.getCompanyByCode(companyCode);
    }
}
