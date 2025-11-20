package com.Product.Controller;

import com.Product.Entity.Product;
import com.Product.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@Tag(name = "Product APIs", description = "CRUD operations for products")
public class ProductController {

    @Autowired
    public ProductService productService;

    @PostMapping("/create")
    @Operation(summary = "Create a new product", description = "Adds a product to the catalog")
    @Tag(name = "Product APIs", description = "CRUD operations for products")
    public ResponseEntity<Product> createProduct (@Valid @RequestBody Product product)
    {

        return ResponseEntity.ok(productService.createProduct(product));
    }

    @GetMapping("/")
    @Operation(summary = "List product", description = "List of products to the catalog")
    public ResponseEntity<List<Product>> getAllProduct()
    {
        return ResponseEntity.ok(productService.getproducts());
    }

    @GetMapping({"/{id}"})
    @Operation(summary = "get a Id product", description = "get a product by id")
    public ResponseEntity<Product> getProduct(@PathVariable Long id ){
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "update a Id product", description = "update a product by id")
    public ResponseEntity<Product>updateProduct(@PathVariable Long id , @RequestBody Product product)
    {
     return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "patch a Id product", description = "patch a product by id")
    public ResponseEntity<Product> updatePartialProduct(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        Product updatedProduct = productService.updatePartialProduct(id, updates);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "remove a Id product", description = "remove a product by id")
    public ResponseEntity<Optional<Product>> removeProduct(@PathVariable Long id )
    {

        Optional<Product> p = productService.removeProduct(id);
        return ResponseEntity.ok(p);
    }

    @GetMapping
    @Operation(summary = "get  all products", description = "get all products")
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(defaultValue = "0") int page  ,
                                                        @RequestParam(defaultValue = "1")int size,
                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                        @RequestParam(defaultValue = "asc") String sortDir){
        return  ResponseEntity.ok(productService.getAllProducts(page, size, sortBy, sortDir));
    }

    @GetMapping("/search")
    @Operation(summary = "search a product", description = "search a product")
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam String keyword,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "1")int size,
                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                        @RequestParam(defaultValue = "asc") String sortDir) {
        return  ResponseEntity.ok(productService.searchProducts(keyword, page, size, sortBy, sortDir));
    }
}
