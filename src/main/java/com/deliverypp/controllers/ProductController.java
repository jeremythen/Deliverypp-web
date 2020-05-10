package com.deliverypp.controllers;

import com.deliverypp.models.Product;
import com.deliverypp.services.product.ProductService;
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

    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(@Autowired ProductServiceImpl productServiceImpl) {
        this.productService = productServiceImpl;
    }

    @GetMapping()
    public ResponseEntity<?> getProducts() {

        DeliveryppResponse<?> response = productService.getProducts();

        return getDefaultResponse(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id) {

        logger.info("getProductById id: {}", id);

        DeliveryppResponse<?> response = productService.getProductById(id);

        return getDefaultResponse(response);

    }

    @PostMapping()
    public ResponseEntity<?> addProduct(@RequestBody @Valid Product product) {

        logger.info("addProduct: {}", product);

        DeliveryppResponse<?> response = productService.addProduct(product);

        return getDefaultResponse(response);

    }

    @PutMapping()
    public ResponseEntity<?> updateProduct(@RequestBody @Valid Product product) {

        DeliveryppResponse<?> response = productService.updateProduct(product);

        return getDefaultResponse(response);

    }

    @PostMapping("/clone")
    public ResponseEntity<?> cloneProduct(@RequestBody @Valid Product product) {

        logger.info("cloneProduct: {}", product);

        DeliveryppResponse<?> response = productService.cloneProduct(product);

        return getDefaultResponse(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {

        logger.info("deleteProduct id: {}", id);

        DeliveryppResponse<?> response = productService.deleteProduct(id);

        return getDefaultResponse(response);

    }

}
