package com.gosquad.infrastructure.persistence.categories;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.infrastructure.persistence.categories.impl.CategoryRepositoryImpl;
import com.gosquad.infrastructure.persistence.utils.TestDatabaseHelper;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryRepositoryTest {

    private CategoryRepositoryImpl repository;

    @BeforeAll
    public void setup() throws SQLException {
        TestDatabaseHelper.setupTestDatabase();
        repository = new CategoryRepositoryImpl();
    }

    @AfterAll
    public void tearDown() throws SQLException {
        TestDatabaseHelper.tearDown();
    }

    @Test
    public void testGetAllByCompanyId() throws Exception {
        List<CategoryModel> categories = repository.getAllByCompanyId(1);
        assertNotNull(categories, "La liste des catégories ne doit pas être null");
        assertEquals(2, categories.size(), "La société 1 doit avoir 2 catégories");
    }

    @Test
    public void testGetById_Success() throws Exception {
        CategoryModel category = repository.getById(1);
        assertNotNull(category, "La catégorie avec ID 1 doit exister");
        assertEquals("Sport", category.getName());
        assertEquals(1, category.getCompanyId());
    }

    @Test
    public void testGetById_NotFound() {
        assertThrows(NotFoundException.class, () -> repository.getById(999));
    }

    @Test
    public void testGetCategoryByNameAndCompanyId_Success() throws Exception {
        CategoryModel category = repository.getCategoryByNameAndCompanyId("Repas", 1);
        assertNotNull(category);
        assertEquals("Repas", category.getName());
    }

    @Test
    public void testGetCategoryByNameAndCompanyId_NotFound() {
        assertThrows(NotFoundException.class, () -> repository.getCategoryByNameAndCompanyId("Inexistant", 1));
    }

    @Test
    public void testCreateCategory() throws Exception {
        CategoryModel newCategory = new CategoryModel(
                null, // ID will be auto-generated
                "Bien-être", // Name
                1 // Company ID
        );

        repository.createCategory(newCategory);

        CategoryModel created = repository.getCategoryByNameAndCompanyId("Bien-être", 1);
        assertNotNull(created);
        assertEquals("Bien-être", created.getName());
    }

    @Test
    public void testUpdateCategory() throws Exception {
        CategoryModel category = repository.getCategoryByNameAndCompanyId("Sport", 1);
        category.setName("Activité Physique");
        repository.updateCategory(category);

        CategoryModel updated = repository.getById(category.getId());
        assertEquals("Activité Physique", updated.getName());
    }

    @Test
    public void testDeleteCategory() throws Exception {
        CategoryModel category = new CategoryModel(
                null,
                "Temporaire", // Name
                1 // Company ID
        );
        repository.createCategory(category);

        CategoryModel created = repository.getCategoryByNameAndCompanyId("Temporaire", 1);
        repository.deleteCategory(created.getId());

        assertThrows(NotFoundException.class, () -> repository.getById(created.getId()));
    }
}
