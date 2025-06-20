package com.gosquad.infrastructure.persistence.customers;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.customers.impl.CustomerRepositoryImpl;
import com.gosquad.infrastructure.persistence.utils.TestDatabaseHelper;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerRepositoryTest {

    private CustomerRepositoryImpl repository;

    @BeforeEach
    public void setup() throws SQLException {
        TestDatabaseHelper.setupTestDatabase();
        repository = new CustomerRepositoryImpl();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        TestDatabaseHelper.tearDown();
    }

    @Test
    public void testGetByIdAndCompanyId_Success() throws SQLException, NotFoundException {
        CustomerModel customer = repository.getByIdAndCompanyId(3, 1);
        assertNotNull(customer, "Le client avec id=3 et companyId=1 doit exister");
        assertEquals("Jean", customer.getFirstname());
    }

    @Test
    public void testGetByIdAndCompanyId_NotFound() {
        assertThrows(NotFoundException.class, () -> repository.getByIdAndCompanyId(9999, 1));
    }

    @Test
    public void testGetAllCustomers_Success() throws ConstraintViolationException {
        List<CustomerModel> customers = repository.getAllCustomers(1);
        assertNotNull(customers, "La liste des clients ne doit pas être nulle");
        assertFalse(customers.isEmpty(), "Il doit y avoir au moins un client pour companyId=1");
    }

    @Test
    public void testAddCustomer_Success() throws SQLException, ConstraintViolationException, NotFoundException {
        CustomerModel newCustomer = new CustomerModel(null, "Alice", "Durand", "alice.durand@example.com", "0123456789", Date.valueOf(java.time.LocalDate.now()), null, null, null, null,null,null,1,1,1,1);

        repository.addCustomer(newCustomer);

        CustomerModel added = repository.getByIdAndCompanyId(newCustomer.getId(), 1);
        assertNotNull(added, "Le client ajouté doit exister");
        assertEquals("Alice", added.getFirstname());
    }

    @Test
    public void testUpdateCustomerPassport_Success() throws SQLException, ConstraintViolationException, NotFoundException {
        CustomerModel customer = repository.getByIdAndCompanyId(3, 1);
        customer.setPassportNumber("AB1234567");

        repository.updateCustomerPassport(customer);

        CustomerModel updated = repository.getByIdAndCompanyId(3, 1);
        assertEquals("AB1234567", updated.getPassportNumber(), "Le numéro de passeport doit avoir été mis à jour");
    }

    @Test
    public void testUpdateCustomerIdCard_Success() throws SQLException, ConstraintViolationException, NotFoundException {
        CustomerModel customer = repository.getByIdAndCompanyId(3, 1);
        customer.setIdCardNumber("ID9876543");

        repository.updateCustomerIdCard(customer);

        CustomerModel updated = repository.getByIdAndCompanyId(3, 1);
        assertEquals("ID9876543", updated.getIdCardNumber(), "Le numéro de carte d'identité doit avoir été mis à jour");
    }

    @Test
    public void testUpdateCustomer_Success() throws SQLException, ConstraintViolationException, NotFoundException {
        CustomerModel customer = repository.getByIdAndCompanyId(3, 1);
        customer.setFirstname("Jean-Modifie");

        repository.updateCustomer(customer);

        CustomerModel updated = repository.getByIdAndCompanyId(3, 1);
        assertEquals("Jean-Modifie", updated.getFirstname(), "Le prénom du client doit avoir été mis à jour");
    }

}
