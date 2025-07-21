package com.gosquad.infrastructure.persistence.groups;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.groups.impl.GroupRepositoryImpl;
import com.gosquad.infrastructure.persistence.utils.TestDatabaseHelper;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GroupRepositoryTest {

    private GroupRepositoryImpl repository;

    @BeforeEach
    public void setup() throws SQLException {
        TestDatabaseHelper.setupTestDatabase();
        repository = new GroupRepositoryImpl();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        TestDatabaseHelper.tearDown();
    }

    @Test
    public void testGetById_Success() throws SQLException, NotFoundException {
        GroupModel group = repository.getById(1);
        assertNotNull(group, "Le groupe avec id=1 doit exister");
        assertEquals("Groupe Paris", group.getName());
    }

    @Test
    public void testGetById_NotFound() {
        assertThrows(NotFoundException.class, () -> repository.getById(9999));
    }

    @Test
    public void testGetAllGroups_Success() throws Exception {
        List<GroupModel> groups = repository.getAllGroups(1);
        assertNotNull(groups, "La liste des groupes ne doit pas être nulle");
        assertFalse(groups.isEmpty(), "Il doit y avoir au moins un groupe pour companyId=1");
    }

    @Test
    public void testAddGroup_Success() throws SQLException, NotFoundException {
        GroupModel newGroup = new GroupModel(null, "Groupe Test", true, LocalDateTime.now(), LocalDateTime.now(), 1);
        repository.addGroup(newGroup);
        GroupModel added = repository.getById(newGroup.getId());
        assertNotNull(added, "Le groupe ajouté doit exister");
        assertEquals("Groupe Test", added.getName());
    }

    @Test
    public void testUpdateGroupName_Success() throws SQLException, NotFoundException {
        GroupModel group = repository.getById(1);
        group.setName("Groupe Modifié");
        repository.updateGroup(group);
        GroupModel updated = repository.getById(1);
        assertEquals("Groupe Modifié", updated.getName(), "Le nom du groupe doit avoir été mis à jour");
    }
}
