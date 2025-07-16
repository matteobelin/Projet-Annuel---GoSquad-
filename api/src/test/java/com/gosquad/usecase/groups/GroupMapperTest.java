package com.gosquad.usecase.groups;

import com.gosquad.domain.groups.GroupEntity;
import com.gosquad.infrastructure.persistence.groups.GroupModel;
import com.gosquad.infrastructure.persistence.groups.GroupModel;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class GroupMapperTest {

    private final GroupMapper mapper = new GroupMapper();

    private GroupModel createSampleModel() {
        return new GroupModel(
                1,
                "Groupe Test",
                true,
                LocalDateTime.of(2024, 1, 1, 10, 0, 0),
                LocalDateTime.of(2024, 1, 2, 10, 0, 0)
        );
    }

    private GroupEntity createSampleEntity() {
        return new GroupEntity(
                1,
                "Groupe Test",
                true,
                LocalDateTime.of(2024, 1, 1, 10, 0, 0),
                LocalDateTime.of(2024, 1, 2, 10, 0, 0)
        );
    }

    @Test
    void testModelToEntity() {
        GroupModel model = createSampleModel();
        GroupEntity entity = mapper.modelToEntity(model);

        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getName(), entity.getName());
        assertEquals(model.getVisible(), entity.getVisible());
        assertEquals(model.getCreatedAt(), entity.getCreatedAt());
        assertEquals(model.getUpdatedAt(), entity.getUpdatedAt());
    }

    @Test
    void testEntityToModel() {
        GroupEntity entity = createSampleEntity();
        GroupModel model = mapper.entityToModel(entity);

        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getVisible(), model.getVisible());
        assertEquals(entity.getCreatedAt(), model.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), model.getUpdatedAt());
    }

    @Test
    void testModelListToEntityList() {
        GroupModel model1 = createSampleModel();
        GroupModel model2 = new GroupModel(
                2,
                "Groupe Test 2",
                false,
                LocalDateTime.of(2024, 2, 1, 10, 0, 0),
                LocalDateTime.of(2024, 2, 2, 10, 0, 0)
        );

        List<GroupModel> models = List.of(model1, model2);

        List<GroupEntity> entities = mapper.modelToEntity(models);

        assertEquals(models.size(), entities.size());

        for (int i = 0; i < models.size(); i++) {
            assertEquals(models.get(i).getId(), entities.get(i).getId());
            assertEquals(models.get(i).getName(), entities.get(i).getName());
            assertEquals(models.get(i).getVisible(), entities.get(i).getVisible());
            assertEquals(models.get(i).getCreatedAt(), entities.get(i).getCreatedAt());
            assertEquals(models.get(i).getUpdatedAt(), entities.get(i).getUpdatedAt());
        }
    }

    @Test
    void testModelToEntity_withNullModel() {
        GroupEntity entity = mapper.modelToEntity((GroupModel) null);
        assertNull(entity);
    }

    @Test
    void testEntityToModel_withNullEntity() {
        GroupModel model = mapper.entityToModel((GroupEntity) null);
        assertNull(model);
    }

    @Test
    void testModelListToEntityList_withNullList() {
        List<GroupEntity> entities = mapper.modelToEntity((List<GroupModel>) null);
        assertNull(entities);
    }

    @Test
    void testModelToEntity_withNullValues() {
        GroupModel model = new GroupModel(
                1,
                "Groupe avec valeurs nulles",
                null,
                null,
                null
        );

        GroupEntity entity = mapper.modelToEntity(model);

        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getName(), entity.getName());
        assertNull(entity.getVisible());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    void testEntityToModel_withNullValues() {
        GroupEntity entity = new GroupEntity(
                1,
                "Groupe avec valeurs nulles",
                null,
                null,
                null
        );

        GroupModel model = mapper.entityToModel(entity);

        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertNull(model.getVisible());
        assertNull(model.getCreatedAt());
        assertNull(model.getUpdatedAt());
    }

    @Test
    void testModelToEntity_withDefaultVisibility() {
        GroupModel model = new GroupModel();
        model.setId(1);
        model.setName("Groupe par défaut");
        // visible sera true par défaut

        GroupEntity entity = mapper.modelToEntity(model);

        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getName(), entity.getName());
        assertTrue(entity.getVisible());
    }

    @Test
    void testEntityToModel_withDefaultVisibility() {
        GroupEntity entity = new GroupEntity(1, "Groupe par défaut");
        // visible sera true par défaut

        GroupModel model = mapper.entityToModel(entity);

        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertTrue(model.getVisible());
    }
}