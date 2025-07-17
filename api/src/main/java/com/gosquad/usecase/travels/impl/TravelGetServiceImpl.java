package com.gosquad.usecase.travels.impl;

import com.gosquad.domain.company.CompanyEntity;
import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.domain.groups.GroupEntity;
import com.gosquad.domain.customers.CustomerEntity;
import com.gosquad.infrastructure.jwt.JWTInterceptor;
import com.gosquad.presentation.DTO.travels.GetAllTravelsResponseDTO;
import com.gosquad.presentation.DTO.travels.GetTravelResponseDTO;
import com.gosquad.presentation.DTO.groups.GroupResponseDTO;
import com.gosquad.presentation.DTO.customers.GetCustomerResponseDTO;
import com.gosquad.usecase.company.CompanyService;
import com.gosquad.usecase.travels.TravelGetService;
import com.gosquad.usecase.travels.TravelService;
import com.gosquad.usecase.groups.GroupService;
import com.gosquad.usecase.customers.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service
public class TravelGetServiceImpl implements TravelGetService {

    private final TravelService travelService;
    private final CompanyService companyService;
    private final GroupService groupService;
    private final CustomerService customerService;
    private final JWTInterceptor jwtInterceptor;

    public TravelGetServiceImpl(TravelService travelService, CompanyService companyService, 
                               GroupService groupService, CustomerService customerService,
                               JWTInterceptor jwtInterceptor) {
        this.travelService = travelService;
        this.companyService = companyService;
        this.groupService = groupService;
        this.customerService = customerService;
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public List<GetAllTravelsResponseDTO> getAllTravels(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = authHeader.substring(7);
            Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);
            String companyCode = tokenInfo.get("companyCode").toString();
            int companyId = companyService.getCompanyByCode(companyCode).getId();
            List<TravelInformationEntity> travels = travelService.getAllTravels(companyId);
            return travels.stream()
                    .map(travel -> {
                        String uniqueId = "TRAVEL" + travel.getId();
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
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public GetTravelResponseDTO getTravel(HttpServletRequest request) throws Exception {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Map<String, Object> tokenInfo = jwtInterceptor.extractTokenInfo(token);

        String companyCode = tokenInfo.get("companyCode").toString();
        String travelId = request.getParameter("id");

        // Extract numeric ID from the uniqueTravelId
        int travelIdInt;
        if (travelId.startsWith("TRAVEL")) {
            travelIdInt = Integer.parseInt(travelId.substring("TRAVEL".length()));
        } else {
            // If no TRAVEL prefix, try to parse as is for backward compatibility
            travelIdInt = Integer.parseInt(travelId);
        }
        
        TravelInformationEntity travel = travelService.getTravelById(travelIdInt);

        // Get the specific group for this travel only
        List<GroupResponseDTO> groups = new ArrayList<>();
        try {
            if (travel.getGroupId() != null) {
                GroupEntity travelGroup = groupService.getGroupById(travel.getGroupId());
                groups.add(new GroupResponseDTO(
                    travelGroup.getId(),
                    travelGroup.getName(),
                    travelGroup.getVisible(),
                    travelGroup.getCreatedAt(),
                    travelGroup.getUpdatedAt()
                ));
            }
        } catch (Exception e) {
            // If group service fails, continue with empty list
            System.err.println("Error fetching travel group: " + e.getMessage());
        }

        // Get participants specific to this travel's group
        List<GetCustomerResponseDTO> participants = new ArrayList<>();
        try {
            if (travel.getGroupId() != null) {
                // Utiliser la méthode existante que j'ai créée pour récupérer les clients du groupe
                List<CustomerEntity> groupCustomers = customerService.getCustomersByGroupId(travel.getGroupId());
                for (CustomerEntity customer : groupCustomers) {
                    // Inclure tous les clients non-anonymes du groupe
                    if (!"anonymous".equalsIgnoreCase(customer.getFirstname())) {
                        participants.add(new GetCustomerResponseDTO(
                            companyCode + customer.getId(),
                            customer.getFirstname(),
                            customer.getLastname(),
                            customer.getEmail(),
                            customer.getPhoneNumber(),
                            customer.getBirthDate(),
                            null, null, null, null, null, null, null, null, null, null,
                            null, null, null, companyCode, null, null
                        ));
                    }
                }
            }
        } catch (Exception e) {
            // Si erreur, continuer avec liste vide mais logger l'erreur
            System.err.println("Error fetching customers for group " + travel.getGroupId() + ": " + e.getMessage());
        }

        return new GetTravelResponseDTO(
                "TRAVEL" + travel.getId(),
                travel.getTitle(),
                travel.getDescription(),
                travel.getStartDate(),
                travel.getEndDate(),
                travel.getDestination(),
                travel.getBudget(),
                travel.getGroupId(),
                travel.getCreatedAt(),
                travel.getUpdatedAt(),
                companyCode,
                groups,
                participants
        );
    }
}