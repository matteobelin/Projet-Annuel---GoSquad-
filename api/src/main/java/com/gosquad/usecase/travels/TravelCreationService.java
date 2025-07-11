package com.gosquad.usecase.travels;

import com.gosquad.domain.travels.TravelInformationEntity;
import com.gosquad.presentation.DTO.travels.VoyageRequestDTO;

public interface TravelCreationService {
    TravelInformationEntity createTravel(VoyageRequestDTO voyageRequest);
}
