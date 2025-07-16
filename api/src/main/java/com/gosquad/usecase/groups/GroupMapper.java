package com.gosquad.usecase.groups;

import com.gosquad.domain.groups.GroupEntity;
import com.gosquad.infrastructure.persistence.groups.GroupModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupMapper {

    public GroupEntity modelToEntity(GroupModel groupModel) {
        if (groupModel == null) {
            return null;
        }
        return new GroupEntity(
                groupModel.getId(),
                groupModel.getName(),
                groupModel.getVisible(),
                groupModel.getCreatedAt(),
                groupModel.getUpdatedAt()
        );
    }

    public GroupModel entityToModel(GroupEntity groupEntity) {
        if (groupEntity == null) {
            return null;
        }
        return new GroupModel(
                groupEntity.getId(),
                groupEntity.getName(),
                groupEntity.getVisible(),
                groupEntity.getCreatedAt(),
                groupEntity.getUpdatedAt()
        );
    }

    public List<GroupEntity> modelToEntity(List<GroupModel> groupModels) {
        if (groupModels == null) {
            return null;
        }
        return groupModels.stream()
                .map(this::modelToEntity)
                .toList();
    }
}
