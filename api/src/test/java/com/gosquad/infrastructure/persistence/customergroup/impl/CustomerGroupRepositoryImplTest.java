package com.gosquad.infrastructure.persistence.customergroup.impl;

import com.gosquad.infrastructure.persistence.customergroup.CustomerGroupRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerGroupRepositoryImplTest {

    @Autowired
    private CustomerGroupRepository customerGroupRepository;

    @Test
    @Order(1)
    void testAddCustomerToGroup() {
        // Suppose que le groupe 1 et le client 1 existent
        assertDoesNotThrow(() -> customerGroupRepository.addCustomerToGroup(1, 1));
        // Pas d'exception = insertion OK
    }
}
