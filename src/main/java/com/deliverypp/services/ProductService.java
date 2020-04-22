package com.deliverypp.services;

import com.deliverypp.models.Product;
import com.deliverypp.repositories.ProductRepository;
import com.deliverypp.util.DeliveryppResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.*;

import static com.deliverypp.util.DeliveryppResponse.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public DeliveryppResponse<List<Product>> getProducts() {

        DeliveryppResponse<List<Product> >response = new DeliveryppResponse<>();

        List<Product> products = productRepository.findAllByVisible(true);

        response.setStatus(SUCCESS)
                .setMessage("Retrieved products successfully")
                .setResponse(products);

        return response;

    }

    public DeliveryppResponse<Product> getProductById(int id) {

        Optional<Product> optionalProduct = productRepository.findByIdAndVisible(id, true);

        DeliveryppResponse<Product> response = new DeliveryppResponse<>();

        if(optionalProduct.isPresent()) {

            response.setStatus(SUCCESS)
                    .setMessage("Product retrieved successfully.")
                    .setResponse(optionalProduct.get());

        } else {
            response.setStatus(ERROR)
                    .setMessage("Product not available.");
        }

        return response;

    }

    public DeliveryppResponse<Product> addProduct(Product product) {

        DeliveryppResponse<Product> response = new DeliveryppResponse<>();

        if(isNull(product)) {
            response
                    .setStatus(ERROR)
                    .setMessage("No product provided.");
            return response;
        }

        productRepository.save(product);

        response
                .setStatus(SUCCESS)
                .setMessage("Product saved successfully.")
                .setResponse(product);

        return response;

    }

    public DeliveryppResponse<Product> updateProduct(Product product) {

        DeliveryppResponse<Product> response = new DeliveryppResponse<>();

        if(nonNull(product)) {
            productRepository.save(product);
            response.setStatus(SUCCESS)
                    .setMessage("Product updated.")
                    .setResponse(product);
        } else {
            response.setStatus(ERROR)
                    .setMessage("Product not provided.");
        }

        return response;

    }

    public DeliveryppResponse<?> deleteProduct(int productId) {

        DeliveryppResponse<Product> response = new DeliveryppResponse<>();

        Optional<Product> optionalProduct = productRepository.findById(productId);

        if(optionalProduct.isPresent()) {

            Product product = optionalProduct.get();

            product.setVisible(false);

            productRepository.save(product);

            response.setStatus(SUCCESS)
                    .setMessage("Product deleted.");

        } else {
            response.setStatus(ERROR)
                    .setMessage("Product does not exist.");
        }

        return response;

    }

}
