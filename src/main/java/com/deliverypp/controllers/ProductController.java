package com.deliverypp.controllers;

import com.deliverypp.models.Product;
import com.deliverypp.services.product.ProductServiceImpl;
import com.deliverypp.util.DeliveryppResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.deliverypp.util.DeliveryppResponse.*;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private ProductServiceImpl productServiceImpl;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(@Autowired ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    @GetMapping()
    public ResponseEntity<?> getProducts() {

        logger.info("getProducts");

        DeliveryppResponse<?> response = productServiceImpl.getProducts();

        return getDefaultResponse(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id) {

        logger.info("getProductById id {}", id);

        DeliveryppResponse<?> response = productServiceImpl.getProductById(id);

        return getDefaultResponse(response);

    }

    @PostMapping()
    public ResponseEntity<?> addProduct(@RequestBody @Valid Product product) {

        logger.info("addProduct {}", product);

        DeliveryppResponse<?> response = productServiceImpl.addProduct(product);

        return getDefaultResponse(response);

    }

    @PutMapping()
    public ResponseEntity<?> updateProduct(@RequestBody @Valid Product product) {

        DeliveryppResponse<Product> productServiceResponse = productServiceImpl.getProductById(product.getId());

        if(productServiceResponse.isSuccess()) {
            logger.info("updateProduct old product {}", productServiceResponse.getResponse());
        }

        DeliveryppResponse<?> response = productServiceImpl.updateProduct(product);

        logger.info("updateProduct new product {}", product);

        return getDefaultResponse(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {

        logger.info("deleteProduct id {}", id);

        DeliveryppResponse<?> response = productServiceImpl.deleteProduct(id);

        return getDefaultResponse(response);

    }

}
