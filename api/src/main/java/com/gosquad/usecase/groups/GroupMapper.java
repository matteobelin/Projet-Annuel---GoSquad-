package com.gosquad.usecase.groups;

import com.gosquad.domain.groups.GroupEntity;
import com.gosquad.infrastructure.persistence.groups.GroupModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupMapper {

    public GroupEntity modelToEntity(GroupModel groupModel) {
        if (groupModel == null) return null;
        GroupEntity entity = new GroupEntity();
        entity.setId(groupModel.getId());
        entity.setName(groupModel.getName());
        entity.setVisible(groupModel.getVisible());
        entity.setCreatedAt(groupModel.getCreatedAt());
        entity.setUpdatedAt(groupModel.getUpdatedAt());
        entity.setCompanyId(groupModel.getCompanyId());
        return entity;
    }

    public GroupModel entityToModel(GroupEntity groupEntity) {
        if (groupEntity == null) return null;
        GroupModel model = new GroupModel();
        model.setId(groupEntity.getId());
        model.setName(groupEntity.getName());
        model.setVisible(groupEntity.getVisible());
        model.setCreatedAt(groupEntity.getCreatedAt());
        model.setUpdatedAt(groupEntity.getUpdatedAt());
        model.setCompanyId(groupEntity.getCompanyId());
        return model;
    }

    public List<GroupEntity> modelToEntity(List<GroupModel> groupModels) {
        if (groupModels == null) return List.of();
        return groupModels.stream()
                .map(this::modelToEntity)
                .toList();
    }
}
