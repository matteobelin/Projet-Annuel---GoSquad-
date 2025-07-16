
package com.gosquad.infrastructure.persistence.travels.impl;

import com.gosquad.infrastructure.persistence.travels.TravelModel;
import org.junit.jupiter.api.Test;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.*;

class TravelRepositoryImplTest {
    @Test
    void testModelCreation() {
        TravelModel model = new TravelModel(
            1,
            "Voyage à Paris",
            "Paris, France",
            Date.valueOf("2024-06-01"),
            Date.valueOf("2024-06-07"),
            1500.0,
            "Un magnifique voyage à Paris avec visite des monuments",
            10,
            1
        );
        assertNotNull(model);
        assertEquals(1, model.getId());
        assertEquals("Voyage à Paris", model.getTitle());
    }
}
