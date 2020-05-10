package com.deliverypp.services.product;

import com.deliverypp.controllers.OrderController;
import com.deliverypp.models.Product;
import com.deliverypp.repositories.ProductRepository;
import com.deliverypp.util.DeliveryppResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.*;

import static com.deliverypp.util.DeliveryppResponse.*;

import static com.deliverypp.util.DeliveryppResponseStatus.*;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public DeliveryppResponse<List<Product>> getProducts() {

        DeliveryppResponse<List<Product> >response = new DeliveryppResponse<>();

        List<Product> products = productRepository.findAllByVisibleOrderByCreatedAtDesc(true);

        response.setStatus(SUCCESS)
                .setMessage("Retrieved products successfully")
                .setResponse(products);

        return response;

    }

    @Transactional
    public DeliveryppResponse<Product> getProductById(int id) {

        Optional<Product> optionalProduct = productRepository.findByIdAndVisible(id, true);

        DeliveryppResponse<Product> response = new DeliveryppResponse<>();

        if(optionalProduct.isPresent()) {

            response.setStatus(SUCCESS)
                    .setMessage("Product retrieved successfully.")
                    .setResponse(optionalProduct.get());

        } else {
            response.setStatus(ERROR)
                    .setSpecificStatus(PRODUCT_NOT_AVAILABLE)
                    .setMessage("Product not available.");
        }

        return response;

    }

    @Transactional
    public DeliveryppResponse<Product> addProduct(Product product) {

        DeliveryppResponse<Product> response = new DeliveryppResponse<>();

        if(isNull(product)) {
            response
                    .setStatus(ERROR)
                    .setSpecificStatus(PRODUCT_EMPTY)
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

    @Transactional
    public DeliveryppResponse<Product> updateProduct(Product product) {

        DeliveryppResponse<Product> response = new DeliveryppResponse<>();

        Optional<Product> oldProductOptional = productRepository.findById(product.getId());



        if(oldProductOptional.isPresent()) {

            Product oldProduct = oldProductOptional.get();
            logger.info("oldProduct: {}", oldProduct);
            if(oldProduct.getPrice() != product.getPrice()) {
                return response.setStatus(ERROR)
                                .setSpecificStatus(PRODUCT_PRICE_CHANGE_ERROR)
                                .setMessage("Product price cannot change. Create a clone product with different price.");
            }
        }

        if(nonNull(product)) {

            logger.info("newProduct: {}", product);

            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
            return response.setStatus(SUCCESS)
                    .setMessage("Product updated.")
                    .setResponse(product);
        } else {
            return response.setStatus(ERROR)
                    .setSpecificStatus(PRODUCT_EMPTY)
                    .setMessage("Product not provided.");
        }

    }

    @Transactional
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
                    .setSpecificStatus(PRODUCT_NOT_AVAILABLE)
                    .setMessage("Product does not exist.");
        }

        return response;

    }

    @Override
    public DeliveryppResponse<Product> cloneProduct(Product product) {

        DeliveryppResponse<Product> response = new DeliveryppResponse<>();

        if(isNull(product)) {
            return response.setStatus(ERROR)
                    .setSpecificStatus(PRODUCT_EMPTY)
                    .setMessage("Product not provided.");
        }

        product.setId(0);
        product.setCreatedAt(LocalDateTime.now());

        Product newProduct = productRepository.save(product);

        return response.setStatus(SUCCESS)
                .setMessage("Product cloned.")
                .setResponse(newProduct);

    }

    @Override
    public boolean existsById(int id) {
        return productRepository.existsByIdAndVisible(id, true);
    }

}
