package com.deliverypp.services.category;

import com.deliverypp.models.Category;
import com.deliverypp.models.Order;
import com.deliverypp.util.DeliveryppResponse;

import java.util.List;

public interface CategoryService {

    DeliveryppResponse<List<Category>> getCategories();
    DeliveryppResponse<Category> getCategoryById(int id);
    DeliveryppResponse<?> addCategory(Category category);
    DeliveryppResponse<?> updateCategory(Category category);
    DeliveryppResponse<?> deleteCategoryById(int id);

}
