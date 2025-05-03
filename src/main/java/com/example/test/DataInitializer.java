package com.example.test;

import com.example.test.entity.Product;
import com.example.test.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner
{
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception
    {
        if (productRepository.count() == 0)
        {
            Product product1 = new Product();
            product1.setName("T-shirt");
            product1.setPrice(15.99);
            product1.setQuantity(20);

            Product product2 = new Product();
            product2.setName("Jeans");
            product2.setPrice(20.99);
            product2.setQuantity(40);

            productRepository.save(product1);
            productRepository.save(product2);

            System.out.println("Data initialized");
        }
    }
}
