package com.deliverypp.services.category;

import com.deliverypp.models.Category;
import com.deliverypp.repositories.CategoryRepository;
import com.deliverypp.util.DeliveryppLoggin;
import com.deliverypp.util.DeliveryppResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.deliverypp.util.DeliveryppResponse.ERROR;
import static com.deliverypp.util.DeliveryppResponse.SUCCESS;

@Service
@DeliveryppLoggin
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public DeliveryppResponse<List<Category>> getCategories() {

        List<Category> categories = categoryRepository.findAll();

        DeliveryppResponse<List<Category>> response = new DeliveryppResponse<>();

        response
                .setStatus(SUCCESS)
                .setMessage("Success retrieving categories.")
                .setResponse(categories);

        return response;

    }

    @Override
    public DeliveryppResponse<Category> getCategoryById(int id) {

        Optional<Category> optionalCategory = categoryRepository.findById(id);

        DeliveryppResponse<Category> response = new DeliveryppResponse<>();

        if(!optionalCategory.isPresent()) {
            return response
                    .setStatus(ERROR)
                    .setMessage("Error finding category.");
        }

        return response
                .setStatus(SUCCESS)
                .setMessage("Success retrieving categories.")
                .setResponse(optionalCategory.get());
    }

    @Override
    public DeliveryppResponse<?> addCategory(Category category) {

        DeliveryppResponse<Category> response = new DeliveryppResponse<>();

        categoryRepository.save(category);

        return response
                .setStatus(SUCCESS)
                .setMessage("Success adding category.")
                .setResponse(category);

    }

    @Override
    public DeliveryppResponse<?> updateCategory(Category category) {

        DeliveryppResponse<Category> response = new DeliveryppResponse<>();

        categoryRepository.save(category);

        return response
                .setStatus(SUCCESS)
                .setMessage("Success updating category.")
                .setResponse(category);

    }

    @Override
    public DeliveryppResponse<?> deleteCategoryById(int id) {

        DeliveryppResponse<Category> response = new DeliveryppResponse<>();

        categoryRepository.deleteById(id);

        return response
                .setStatus(SUCCESS)
                .setMessage("Success deleting category.");

    }
}
