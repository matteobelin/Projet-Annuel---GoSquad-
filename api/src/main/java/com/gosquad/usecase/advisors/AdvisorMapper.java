package com.gosquad.usecase.advisors;
import com.gosquad.domain.advisors.AdvisorEntity;
import com.gosquad.infrastructure.persistence.advisors.AdvisorModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdvisorMapper {

    public AdvisorEntity modelToEntity(AdvisorModel advisorModel) {
        return new AdvisorEntity(
                advisorModel.getId(),
                advisorModel.getFirstname(),
                advisorModel.getLastname(),
                advisorModel.getEmail(),
                advisorModel.getPhone(),
                advisorModel.getRole(),
                advisorModel.getCompagnyId()
        );
    }

    public List<AdvisorEntity> modelToEntity(List<AdvisorModel> advisorModelList) {
        List<AdvisorEntity> advisorEntities = new ArrayList<>();
        for (AdvisorModel advisorModel : advisorModelList) {
            advisorEntities.add(modelToEntity(advisorModel));
        }
        return advisorEntities;
    }

}
