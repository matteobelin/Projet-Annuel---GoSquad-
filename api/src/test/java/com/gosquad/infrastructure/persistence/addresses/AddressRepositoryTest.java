package com.gosquad.infrastructure.persistence.addresses;

import com.gosquad.core.exceptions.ConstraintViolationException;
import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.addresses.impl.AddressRepositoryImpl;
import com.gosquad.infrastructure.persistence.utils.TestDatabaseHelper;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddressRepositoryTest {

    private AddressRepositoryImpl repository;

    @BeforeEach
    public void setup() throws SQLException {
        TestDatabaseHelper.setupTestDatabase();
        repository = new AddressRepositoryImpl();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        TestDatabaseHelper.tearDown();
    }

    @Test
    public void testGetById_Success() throws SQLException, NotFoundException {
        AddressModel address = repository.getById(1);
        assertNotNull(address, "L'adresse avec id=1 doit exister");
        assertEquals("10 Boulevard Haussmann", address.getAddressLine());
        assertEquals(2, address.getCityId());
    }

    @Test
    public void testGetById_NotFound() {
        assertThrows(NotFoundException.class, () -> repository.getById(9999));
    }

    @Test
    public void testGetByAddressLineByCityId_Success() throws SQLException, ConstraintViolationException, NotFoundException {
        AddressModel address = repository.getByAddressLineByCityId("10 Boulevard Haussmann", 2);
        assertNotNull(address, "L'adresse avec cette ligne et cityId doit exister");
        assertEquals(1, address.getId());
    }

    @Test
    public void testGetByAddressLineByCityId_NotFound() {
        assertThrows(NotFoundException.class, () -> repository.getByAddressLineByCityId("Adresse Inexistante", 99));
    }

    @Test
    public void testAddAddress_Success() throws SQLException, NotFoundException {
        AddressModel newAddress = new AddressModel(null,"456 Rue Nouvelle", 2);

        repository.addAddress(newAddress);

        AddressModel added = repository.getByAddressLineByCityId("456 Rue Nouvelle", 2);
        assertNotNull(added, "L'adresse ajoutée doit exister");
        assertEquals("456 Rue Nouvelle", added.getAddressLine());
    }

    @Test
    public void testUpdateAddress_Success() throws SQLException, ConstraintViolationException, NotFoundException {
        AddressModel address = repository.getById(1);
        address.setAddressLine("123 Rue Modifiee");

        repository.updateAddress(address);

        AddressModel updated = repository.getById(1);
        assertEquals("123 Rue Modifiee", updated.getAddressLine(), "L'adresse doit avoir été mise à jour");
    }

    @Test
    public void testUpdateAddress_ConstraintViolation() {
        AddressModel invalidAddress = new AddressModel(1,null, 1);


        assertThrows(ConstraintViolationException.class, () -> repository.updateAddress(invalidAddress));
    }
}
