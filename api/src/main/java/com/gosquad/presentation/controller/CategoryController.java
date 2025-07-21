package com.gosquad.presentation.controller;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.presentation.DTO.categories.CategoryRequestDTO;
import com.gosquad.presentation.DTO.categories.CategoryResponseDTO;
import com.gosquad.usecase.categories.CategoryDeleteService;
import com.gosquad.usecase.categories.CategoryGetService;
import com.gosquad.usecase.categories.CategoryPostService;
import com.gosquad.usecase.categories.CategoryUpdateService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {
    private final CategoryGetService categoryGetService;
    private final CategoryPostService categoryPostService;
    private final CategoryUpdateService categoryUpdateService;
    private final CategoryDeleteService categoryDeleteService;

    public CategoryController(CategoryGetService categoryGetService, CategoryPostService categoryPostService, CategoryUpdateService categoryUpdateService, CategoryDeleteService categoryDeleteService) {
        this.categoryGetService = categoryGetService;
        this.categoryPostService = categoryPostService;
        this.categoryUpdateService = categoryUpdateService;
        this.categoryDeleteService = categoryDeleteService;
    }


    @GetMapping("/category/all")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(HttpServletRequest request) {
        try {
            List<CategoryResponseDTO> categories = categoryGetService.getAllCategories(request);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of());
        }
    }

    @GetMapping("/category/by-id")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(HttpServletRequest request) {
        try {
            CategoryResponseDTO category = categoryGetService.getCategoryById(request);
            return ResponseEntity.ok(category);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/category/by-name")
    public ResponseEntity<CategoryResponseDTO> getCategoryByNameAndCompanyId(HttpServletRequest request) {
        try {
            CategoryResponseDTO category = categoryGetService.getCategoryByNameAndCompanyId(request);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/category")
    public ResponseEntity<Map<String, String>> createCategory(
            HttpServletRequest request,
            @RequestBody CategoryRequestDTO dto) {
        try {
            categoryPostService.createCategory(request, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Category created successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message",e.getMessage()));
        }
    }


    @PutMapping("/category")
    public ResponseEntity<Map<String, String>> updateCategory(
            HttpServletRequest request,
            @RequestBody CategoryRequestDTO dto) {
        try {
            categoryUpdateService.updateCategory(request, dto);
            return ResponseEntity.ok(Map.of("message", "Category updated successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message",e.getMessage()));
        }
    }


    @DeleteMapping("/category")
    public ResponseEntity<Map<String, String>> deleteCategory(
            HttpServletRequest request,
            @RequestBody CategoryRequestDTO dto) {
        try {
            categoryDeleteService.deleteCategory(request, dto);
            return ResponseEntity.ok(Map.of("message","Category deleted successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message",e.getMessage()));
        }
    }
}
