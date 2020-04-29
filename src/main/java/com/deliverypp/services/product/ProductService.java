package com.deliverypp.services.product;

import com.deliverypp.models.Product;
import com.deliverypp.util.DeliveryppResponse;

import java.util.List;

public interface ProductService {

    DeliveryppResponse<List<Product>> getProducts();

    DeliveryppResponse<Product> getProductById(int id);

    DeliveryppResponse<Product> addProduct(Product product);

    DeliveryppResponse<Product> updateProduct(Product product);

    DeliveryppResponse<?> deleteProduct(int productId);

    boolean existsById(int id);

}
