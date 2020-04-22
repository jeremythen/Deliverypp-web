package com.deliverypp.controllers;

import com.deliverypp.models.Product;
import com.deliverypp.services.ProductService;
import com.deliverypp.util.DeliveryppResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.deliverypp.util.DeliveryppResponse.*;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    public ResponseEntity<DeliveryppResponse<?>> getProducts() {

        DeliveryppResponse<?> response = productService.getProducts();

        return getDefaultResponse(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryppResponse<?>> getProductById(@PathVariable int id) {

        DeliveryppResponse<?> response = productService.getProductById(id);

        return getDefaultResponse(response);

    }

    @PostMapping()
    public ResponseEntity<DeliveryppResponse<?>> addProduct(@RequestBody @Valid Product product) {

        DeliveryppResponse<?> response = productService.addProduct(product);

        return getDefaultResponse(response);

    }

    @PutMapping()
    public ResponseEntity<DeliveryppResponse<?>> updateProduct(@RequestBody @Valid Product product) {

        DeliveryppResponse<?> response = productService.updateProduct(product);

        return getDefaultResponse(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable int id) {

        DeliveryppResponse<?> response = productService.deleteProduct(id);

        return getDefaultResponse(response);

    }

}
