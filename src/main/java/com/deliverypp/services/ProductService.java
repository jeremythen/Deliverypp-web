package com.deliverypp.services;

import com.deliverypp.models.Product;
import com.deliverypp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {

        if(isNull(product)) {
            return product;
        }

        Product savedProduct = productRepository.save(product);

        return savedProduct;

    }

    public List<Product> getProducts() {

        return productRepository.findAll();

    }

    public void updateProduct(Product product) {

        if(nonNull(product)) {
            productRepository.save(product);
        }

    }

    public void deleteProduct(int productId) {

        productRepository.deleteById(productId);

    }

}
