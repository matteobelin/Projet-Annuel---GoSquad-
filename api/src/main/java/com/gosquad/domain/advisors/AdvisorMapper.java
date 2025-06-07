package com.gosquad.domain.advisors;
import com.gosquad.data.advisors.AdvisorModel;
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
                advisorModel.getRole()
        );
    }

    public List<AdvisorEntity> modelToEntity(List<AdvisorModel> advisorModelList) {
        List<AdvisorEntity> advisorEntities = new ArrayList<>();
        for (AdvisorModel advisorModel : advisorModelList) {
            advisorEntities.add(modelToEntity(advisorModel));
        }
        return advisorEntities;
    }

    public AdvisorModel entityToModel(AdvisorEntity advisorEntity) {
        return new AdvisorModel(
                advisorEntity.getId(),
                advisorEntity.getFirstname(),
                advisorEntity.getLastname(),
                advisorEntity.getEmail(),
                advisorEntity.getPhone(),
                null,
                null,
                advisorEntity.getRole()
        );
    }

    public List<AdvisorModel> entityToModel(List<AdvisorEntity> advisorEntities) {
        List<AdvisorModel> advisorModels = new ArrayList<>();

        for (AdvisorEntity advisorEntity : advisorEntities) {
            advisorModels.add(entityToModel(advisorEntity));
        }
        return advisorModels;
    }
}
