package com.gosquad.infrastructure.persistence.activities_customers;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.activities_customers.impl.ActivityCustomerImpl;
import com.gosquad.infrastructure.persistence.utils.TestDatabaseHelper;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActivityCustomerRepositoryTest {

    private ActivityCustomerRepository repository;

    @BeforeAll
    public void setup() throws SQLException {
        TestDatabaseHelper.setupTestDatabase();
        repository = new ActivityCustomerImpl(); // Ton implémentation
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
    public void testGetActivitiesByGroupIdWhereParticipation() throws SQLException {
        List<ActivityCustomerModel> activities = repository.getActivitiesByGroupIdWhereParticipation(1, true);
        assertNotNull(activities);
        assertFalse(activities.isEmpty());
        for (ActivityCustomerModel ac : activities) {
            assertEquals(1, ac.getGroupId());
            assertTrue(ac.getParticipation());
        }
    }

    @Test
    public void testGetActivityByCustomerId() throws SQLException {
        List<ActivityCustomerModel> activities = repository.getActivityByCustomerId(3);
        assertNotNull(activities);
        assertFalse(activities.isEmpty());
        for (ActivityCustomerModel ac : activities) {
            assertEquals(3, ac.getCustomerId());
        }
    }

    @Test
    public void testGetActivityByCustomerWhereParticipation() throws SQLException, NotFoundException {
        List<ActivityCustomerModel> activities = repository.getActivityByCustomerWhereParticipation(6, true, 1);
        assertNotNull(activities);
        assertFalse(activities.isEmpty());
        for (ActivityCustomerModel ac : activities) {
            assertEquals(6, ac.getCustomerId());
            assertEquals(1, ac.getGroupId());
            assertTrue(ac.getParticipation());
        }
    }

    @Test
    public void testGetActivityById() throws SQLException, NotFoundException {
        ActivityCustomerModel ac = repository.getActivityById(1, 3, 1);
        assertNotNull(ac);
        assertEquals(1, ac.getActivityId());
        assertEquals(3, ac.getCustomerId());
        assertEquals(1, ac.getGroupId());
    }

    @Test
    public void testGetActivityByCustomerIdAndGroupIdWhereParticipation() throws SQLException {
        List<ActivityCustomerModel> activities = repository.getActivityByCustomerIdAndGroupIdWhereParticipation(3, 1, true);
        assertNotNull(activities);
        assertFalse(activities.isEmpty());
        for (ActivityCustomerModel ac : activities) {
            assertEquals(3, ac.getCustomerId());
            assertEquals(1, ac.getGroupId());
            assertTrue(ac.getParticipation());
        }
    }

    @Test
    public void testGetCustomersByActivityIdAndGroupId() throws SQLException {
        List<ActivityCustomerModel> customers = repository.getCustomersByActivityIdAndGroupId(1, 1);
        assertNotNull(customers);
        assertFalse(customers.isEmpty());
        for (ActivityCustomerModel ac : customers) {
            assertEquals(1, ac.getActivityId());
            assertEquals(1, ac.getGroupId());
        }
    }


}
