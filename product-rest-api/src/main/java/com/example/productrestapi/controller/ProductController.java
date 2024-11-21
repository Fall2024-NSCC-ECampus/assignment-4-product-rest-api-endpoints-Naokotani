package com.example.productrestapi.controller;

import com.example.productrestapi.model.Product;
import com.example.productrestapi.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

/**
 * CRUD controller for a product
 */
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Create a new product
     * @param product {@link Product} to be created.
     * @return the new created {@link Product}
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        productRepository.save(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    /**
     * Get a {@link Product} by id.
     * @param id of the {@link Product} to retrieve from database.
     * @return {@link Product} retrieved from the database.
     * @throws ResourceAccessException throws if product by that id is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) throws ResourceAccessException {
        return new ResponseEntity<>(productRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND)), HttpStatus.OK);
    }

    /**
     * Gets all {@link Product}
     * @return A list of all {@link Product}
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    /**
     * Deletes a product from the database by id.
     * @param id of the product ot be deleted.
     * @return delete message
     * @throws HttpClientErrorException if the product does not exist in the database.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id) throws HttpClientErrorException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        productRepository.delete(product);
        return new ResponseEntity<>("Product Deleted Successfully", HttpStatus.OK);
    }

    /**
     * Updates a product in the database.
     * @param product new {@link Product} data.
     * @param id of the product to be updated.
     * @return updated product.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable Long id)
    throws HttpClientErrorException, ResourceAccessException {
        productRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        product.setId(id);
        return new ResponseEntity<>(productRepository.save(product), HttpStatus.OK);
    }
}
