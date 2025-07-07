package com.gosquad.infrastructure.persistence.activities;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.activities.impl.ActivityRepositoryImpl;
import com.gosquad.infrastructure.persistence.utils.TestDatabaseHelper;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActivityRepositoryTest {

    private ActivityRepository repository;

    @BeforeAll
    public void setup() throws SQLException {
        TestDatabaseHelper.setupTestDatabase();
        repository = new ActivityRepositoryImpl();
    }

    @BeforeEach
    public void resetDb() throws SQLException {
        TestDatabaseHelper.setupTestDatabase();
    }

    @AfterAll
    public void tearDown() throws SQLException {
        TestDatabaseHelper.tearDown();
    }

    @Test
    public void testGetAllByCompanyId() throws Exception {
        List<ActivityModel> activities = repository.getAllByCompanyId(1);
        assertNotNull(activities);
        assertFalse(activities.isEmpty());
        for (ActivityModel activity : activities) {
            assertEquals(1, activity.getCompanyId());
        }
    }

    @Test
    public void testGetAllActivitiesByCategory() throws SQLException {
        List<ActivityModel> activities = repository.getAllActivitiesByCategory(2);
        assertNotNull(activities);
        assertFalse(activities.isEmpty());
        for (ActivityModel activity : activities) {
            assertEquals(2, activity.getCategoryId());
        }
    }

    @Test
    public void testGetByIdAndCompanyId_Success() throws Exception {
        ActivityModel activity = repository.getByIdAndCompanyId(1, 1);
        assertNotNull(activity);
        assertEquals(1, activity.getId());
        assertEquals(1, activity.getCompanyId());
    }

    @Test
    public void testGetByIdAndCompanyId_NotFound() {
        assertThrows(NotFoundException.class, () -> repository.getByIdAndCompanyId(9999, 1));
        assertThrows(NotFoundException.class, () -> repository.getByIdAndCompanyId(1, 9999));
    }

    @Test
    public void testCreateActivity() throws SQLException, Exception {
        ActivityModel newActivity = new ActivityModel(
                null,               // id
                "New Activity",     // name
                "Description here", // description
                1,                  // addressId
                1,                  // priceId
                2,                  // categoryId
                1                   // companyId
        );

        repository.createActivity(newActivity);
        assertNotNull(newActivity.getId());

        ActivityModel created = repository.getByIdAndCompanyId(newActivity.getId(), 1);
        assertEquals("New Activity", created.getName());
        assertEquals("Description here", created.getDescription());
        assertEquals(1, created.getAddressId());
        assertEquals(1, created.getPriceId());
        assertEquals(2, created.getCategoryId());
        assertEquals(1, created.getCompanyId());
    }

    @Test
    public void testUpdateActivity() throws Exception {
        ActivityModel activity = repository.getByIdAndCompanyId(1, 1);
        activity.setName("Updated Name");
        activity.setDescription("Updated Description");
        repository.updateActivity(activity);

        ActivityModel updated = repository.getByIdAndCompanyId(1, 1);
        assertEquals("Updated Name", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
    }

    @Test
    public void testDeleteActivity() throws Exception {
        ActivityModel newActivity = new ActivityModel(
                null,
                "To Delete",
                "Desc to delete",
                1,
                1,
                2,
                1
        );
        repository.createActivity(newActivity);

        int idToDelete = newActivity.getId();
        repository.deleteActivity(idToDelete);

        assertThrows(NotFoundException.class, () -> repository.getByIdAndCompanyId(idToDelete, 5));
    }
}
