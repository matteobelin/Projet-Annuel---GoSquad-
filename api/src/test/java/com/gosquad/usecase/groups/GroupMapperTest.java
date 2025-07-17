package com.gosquad.usecase.groups;

import com.gosquad.domain.groups.GroupEntity;
import com.gosquad.infrastructure.persistence.groups.GroupModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class GroupMapperTest {

    private final GroupMapper mapper = new GroupMapper();

    private GroupModel createSampleModel() {
        return new GroupModel(1, "Groupe Test", true, null, null, 10);
    }

    private GroupEntity createSampleEntity() {
        return new GroupEntity(1, "Groupe Test", 10);
    }

    @Test
    void testModelToEntity() {
        GroupModel model = createSampleModel();
        GroupEntity entity = mapper.modelToEntity(model);

        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getName(), entity.getName());
        assertEquals(model.getVisible(), entity.getVisible());
        assertEquals(model.getCompanyId(), entity.getCompanyId());
    }

    @Test
    void testEntityToModel() {
        GroupEntity entity = createSampleEntity();
        GroupModel model = mapper.entityToModel(entity);

        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getVisible(), model.getVisible());
        assertEquals(entity.getCompanyId(), model.getCompanyId());
    }

    @Test
    void testModelListToEntityList() {
        GroupModel model1 = createSampleModel();
        GroupModel model2 = new GroupModel(2, "Groupe Deux", false, null, null, 20);
        List<GroupModel> models = List.of(model1, model2);

        List<GroupEntity> entities = mapper.modelToEntity(models);
        assertEquals(models.size(), entities.size());
        for (int i = 0; i < models.size(); i++) {
            assertEquals(models.get(i).getId(), entities.get(i).getId());
            assertEquals(models.get(i).getName(), entities.get(i).getName());
            assertEquals(models.get(i).getVisible(), entities.get(i).getVisible());
            assertEquals(models.get(i).getCompanyId(), entities.get(i).getCompanyId());
        }
    }
}
