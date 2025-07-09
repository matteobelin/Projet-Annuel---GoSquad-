package com.gosquad.usecase.travels.impl;

import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import com.gosquad.presentation.DTO.travels.GetAllTravelsResponseDTO;
import com.gosquad.presentation.DTO.travels.GetTravelResponseDTO;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.usecase.travels.TravelGetService;
import com.gosquad.usecase.travels.TravelService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TravelGetServiceImpl implements TravelGetService {

    private final TravelService travelService;
    private final CompanyService companyService;
    private final JWTInterceptor jwtInterceptor;

    public TravelGetServiceImpl(TravelService travelService, CompanyService companyService, JWTInterceptor jwtInterceptor) {
        this.travelService = travelService;
        this.companyService = companyService;
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public List<GetAllTravelsResponseDTO> getAllTravels(HttpServletRequest request) throws Exception {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);

        String companyCode = tokenInfo.get("companyCode").toString();
        CompanyEntity company = companyService.getCompanyByCode(companyCode);

        List<TravelInformationEntity> travels = travelService.getAllTravels();

        return travels.stream()
                .map(travel -> {
                    String uniqueId = companyCode + travel.getId();
                    return new GetAllTravelsResponseDTO(
                            uniqueId,
                            travel.getTitle(),
                            travel.getDestination(),
                            travel.getStartDate(),
                            travel.getEndDate(),
                            travel.getBudget()
                    );
                })
                .toList();
    }

    @Override
    public GetTravelResponseDTO getTravel(HttpServletRequest request) throws Exception {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);

        String companyCode = tokenInfo.get("companyCode").toString();
        String travelNumber = request.getParameter("travelNumber");

        int travelId = Integer.parseInt(travelNumber.replaceAll(companyCode, ""));
        CompanyEntity company = companyService.getCompanyByCode(companyCode);
        
        TravelInformationEntity travel = travelService.getTravelById(travelId);

        return new GetTravelResponseDTO(
                companyCode + travel.getId(),
                travel.getTitle(),
                travel.getDescription(),
                travel.getStartDate(),
                travel.getEndDate(),
                travel.getDestination(),
                travel.getBudget(),
                travel.getGroupId(),
                travel.getCreatedAt(),
                travel.getUpdatedAt(),
                companyCode
        );
    }
}