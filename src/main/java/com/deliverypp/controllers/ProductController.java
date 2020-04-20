package com.deliverypp.controllers;

import com.deliverypp.models.Product;
import com.deliverypp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    public List<Product> getProducts() {

        return productService.getProducts();

    }

    @PostMapping()
    public ResponseEntity<Product> addProduct(@RequestBody @Valid Product product) {

        Product savedProduct = productService.addProduct(product);

        return ResponseEntity.ok(savedProduct);

    }

    @PutMapping()
    public ResponseEntity updateProduct(@RequestBody @Valid Product product) {

        productService.updateProduct(product);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable int id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();

    }

}
