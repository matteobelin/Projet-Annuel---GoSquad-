package com.gosquad.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gosquad.usecase.customergroup.CustomerGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerGroupControllerTest {

    private MockMvc mockMvc;
    private CustomerGroupService customerGroupService;
    private CustomerGroupController customerGroupController;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        customerGroupService = mock(CustomerGroupService.class);
        customerGroupController = new CustomerGroupController(customerGroupService);
        mockMvc = MockMvcBuilders.standaloneSetup(customerGroupController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddCustomerToGroup_Success() throws Exception {
        CustomerGroupController.AddCustomerToGroupRequest request = 
            new CustomerGroupController.AddCustomerToGroupRequest(123);

        doNothing().when(customerGroupService).addCustomerToGroup(123, 1);

        mockMvc.perform(post("/group/1/add-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().json("{\"message\": \"Client ajouté au groupe avec succès\"}"));

        verify(customerGroupService, times(1)).addCustomerToGroup(123, 1);
    }

    @Test
    void testAddCustomerToGroup_ServiceException() throws Exception {
        CustomerGroupController.AddCustomerToGroupRequest request = 
            new CustomerGroupController.AddCustomerToGroupRequest(123);

        doThrow(new RuntimeException("Erreur de base de données"))
                .when(customerGroupService).addCustomerToGroup(123, 1);

        mockMvc.perform(post("/group/1/add-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().json("{\"error\": \"Erreur lors de l'ajout du client au groupe: Erreur de base de données\"}"));

        verify(customerGroupService, times(1)).addCustomerToGroup(123, 1);
    }

    @Test
    void testAddCustomerToGroup_WithDifferentGroupId() throws Exception {
        CustomerGroupController.AddCustomerToGroupRequest request = 
            new CustomerGroupController.AddCustomerToGroupRequest(456);

        doNothing().when(customerGroupService).addCustomerToGroup(456, 5);

        mockMvc.perform(post("/group/5/add-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().json("{\"message\": \"Client ajouté au groupe avec succès\"}"));

        verify(customerGroupService, times(1)).addCustomerToGroup(456, 5);
    }

    @Test
    void testRemoveCustomerFromGroup_Success() throws Exception {
        doNothing().when(customerGroupService).removeCustomerFromGroup(123, 1);

        mockMvc.perform(delete("/group/1/remove-customer/123"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().json("{\"message\": \"Client retiré du groupe avec succès\"}"));

        verify(customerGroupService, times(1)).removeCustomerFromGroup(123, 1);
    }

    @Test
    void testRemoveCustomerFromGroup_ServiceException() throws Exception {
        doThrow(new RuntimeException("Client non trouvé dans le groupe"))
                .when(customerGroupService).removeCustomerFromGroup(123, 1);

        mockMvc.perform(delete("/group/1/remove-customer/123"))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().json("{\"error\": \"Erreur lors de la suppression du client du groupe: Client non trouvé dans le groupe\"}"));

        verify(customerGroupService, times(1)).removeCustomerFromGroup(123, 1);
    }

    @Test
    void testRemoveCustomerFromGroup_WithDifferentIds() throws Exception {
        doNothing().when(customerGroupService).removeCustomerFromGroup(789, 3);

        mockMvc.perform(delete("/group/3/remove-customer/789"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().json("{\"message\": \"Client retiré du groupe avec succès\"}"));

        verify(customerGroupService, times(1)).removeCustomerFromGroup(789, 3);
    }

    @Test
    void testAddCustomerToGroup_WithNullPointerException() throws Exception {
        CustomerGroupController.AddCustomerToGroupRequest request = 
            new CustomerGroupController.AddCustomerToGroupRequest(123);

        doThrow(new NullPointerException("Référence nulle"))
                .when(customerGroupService).addCustomerToGroup(123, 1);

        mockMvc.perform(post("/group/1/add-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().json("{\"error\": \"Erreur lors de l'ajout du client au groupe: Référence nulle\"}"));

        verify(customerGroupService, times(1)).addCustomerToGroup(123, 1);
    }

    @Test
    void testRemoveCustomerFromGroup_WithIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("ID invalide"))
                .when(customerGroupService).removeCustomerFromGroup(123, 1);

        mockMvc.perform(delete("/group/1/remove-customer/123"))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().json("{\"error\": \"Erreur lors de la suppression du client du groupe: ID invalide\"}"));

        verify(customerGroupService, times(1)).removeCustomerFromGroup(123, 1);
    }

    @Test
    void testAddCustomerToGroup_WithZeroCustomerId() throws Exception {
        CustomerGroupController.AddCustomerToGroupRequest request = 
            new CustomerGroupController.AddCustomerToGroupRequest(0);

        doNothing().when(customerGroupService).addCustomerToGroup(0, 1);

        mockMvc.perform(post("/group/1/add-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().json("{\"message\": \"Client ajouté au groupe avec succès\"}"));

        verify(customerGroupService, times(1)).addCustomerToGroup(0, 1);
    }

    @Test
    void testRemoveCustomerFromGroup_WithZeroIds() throws Exception {
        doNothing().when(customerGroupService).removeCustomerFromGroup(0, 0);

        mockMvc.perform(delete("/group/0/remove-customer/0"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().json("{\"message\": \"Client retiré du groupe avec succès\"}"));

        verify(customerGroupService, times(1)).removeCustomerFromGroup(0, 0);
    }

    @Test
    void testAddCustomerToGroupRequest_DefaultConstructor() {
        CustomerGroupController.AddCustomerToGroupRequest request = 
            new CustomerGroupController.AddCustomerToGroupRequest();
        
        // Test que l'objet peut être créé sans erreur
        assertNotNull(request);
    }

    @Test
    void testAddCustomerToGroupRequest_ParameterizedConstructor() {
        CustomerGroupController.AddCustomerToGroupRequest request = 
            new CustomerGroupController.AddCustomerToGroupRequest(42);
        
        // Test que l'objet peut être créé avec un paramètre
        assertNotNull(request);
        assertEquals(42, request.customerId);
    }

    private void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError("Expected non-null object");
        }
    }

    private void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("Expected: " + expected + " but was: " + actual);
        }
    }
}