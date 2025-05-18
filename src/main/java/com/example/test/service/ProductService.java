package com.example.test.service;

import com.example.test.entity.Product;

import com.example.test.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService
{
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    public List<Product> getAll()
    {
        return productRepository.findAll();
    }

    public Optional<Product> getById(Long id)
    {
        return productRepository.findById(id);
    }

    public List<Product> getByCategory(String category)
    {
        return productRepository.findByCategory(category);
    }

    public Product create(Product product)
    {
        return productRepository.save(product);
    }

    public Product update(Long id, Product updatedProduct)
    {
        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());
            return productRepository.save(product);
        }).orElse(null);
    }

    public void delete(Long id)
    {
        productRepository.deleteById(id);
    }
}
