package com.gosquad.infrastructure.persistence.customergroup.impl;

import com.gosquad.infrastructure.persistence.customergroup.CustomerGroupRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@org.springframework.test.context.ActiveProfiles("test")
@org.springframework.test.context.TestPropertySource(properties = {"test.env=true"})
public class CustomerGroupRepositoryImplTest {


    @Autowired
    private CustomerGroupRepository customerGroupRepository;

    @Test
    @Order(1)
    void testAddCustomerToGroup() {
        try {
            customerGroupRepository.addCustomerToGroup(2, 1);
        } catch (Exception e) {
            if (e.getMessage() != null && (e.getMessage().toLowerCase().contains("unique") || e.getMessage().toLowerCase().contains("duplicate"))) {
                return;
            }
            throw e;
        }
    }
}
