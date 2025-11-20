package com.Product.Service;

import com.Product.DTO.ProductCreatedEvent;
import com.Product.Entity.Product;
import com.Product.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;
import com.Product.Kafka.ProductEventProducer;
@Service
public class ProductService {

    @Autowired
    public ProductRepository productRepository;

    @Autowired
    private ProductEventProducer productEventProducer;

    public Product  createProduct (Product product){
        Product savedProduct = productRepository.save(product);
         ProductCreatedEvent event = ProductCreatedEvent.builder().
                 id(savedProduct.getId())
                 .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .stock(savedProduct.getStock())
                .build();
        productEventProducer.sendProductCreatedEvent(event);
        return savedProduct;
    }

    public List<Product> getproducts()
    {
    return  productRepository.findAll();
    }


    public Product getProduct(Long id)
    {
        return productRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    public Product updateProduct(Long id , Product product)
    {
        Product existingProduct =  productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());

        return productRepository.save(existingProduct);
    }
    public Product updatePartialProduct(Long id, Map<String, Object> updates) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        // Dynamically update only provided fields
        if (updates.containsKey("name")) {
            existingProduct.setName((String) updates.get("name"));
        }
        if (updates.containsKey("description")) {
            existingProduct.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("price")) {
            existingProduct.setPrice(Double.valueOf(updates.get("price").toString()));
        }
        if (updates.containsKey("stock")) {
            existingProduct.setStock(Integer.valueOf(updates.get("stock").toString()));
        }

        return productRepository.save(existingProduct);
    }

    public Optional<Product>  removeProduct(Long id)
    {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("❌ Product not found with id: " + id);
        }
     Optional<Product> p=   productRepository.findById(id);
         productRepository.deleteById(id);
        return p;

    }

    public Page<Product> getAllProducts( int page , int size, String sortBy, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size,sort);
        return  productRepository.findAll(pageable);
    }

    public Page<Product> searchProducts(String keyword, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }
}
