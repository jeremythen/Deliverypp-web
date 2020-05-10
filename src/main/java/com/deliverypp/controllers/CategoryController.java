package com.deliverypp.controllers;

import com.deliverypp.models.Category;
import com.deliverypp.services.category.CategoryService;
import com.deliverypp.util.DeliveryppResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.deliverypp.util.DeliveryppResponse.getDefaultResponse;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    private CategoryService categoryService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<?> getCategories() {

        logger.info("getCategories.");

        DeliveryppResponse<List<Category>> response = categoryService.getCategories();

        return getDefaultResponse(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable int id) {

        logger.info("getCategoryById id: {}", id);

        DeliveryppResponse<?> response = categoryService.getCategoryById(id);

        return getDefaultResponse(response);

    }

    @PostMapping()
    public ResponseEntity<?> addCategory(@RequestBody @Valid Category category) {

        logger.info("addCategory: {}", category);

        DeliveryppResponse<?> response = categoryService.addCategory(category);

        return getDefaultResponse(response);

    }

    @PutMapping()
    public ResponseEntity<?> updateCategory(@RequestBody @Valid Category category) {

        DeliveryppResponse<?> response = categoryService.updateCategory(category);

        logger.info("updateCategory new category: {}", category);

        return getDefaultResponse(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable int id) {

        logger.info("deleteCategoryById id: {}", id);

        DeliveryppResponse<?> response = categoryService.deleteCategoryById(id);

        return getDefaultResponse(response);

    }

}
