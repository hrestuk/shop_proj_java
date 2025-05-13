package com.example.test;

import com.example.test.entity.Product;
import com.example.test.entity.Roles;
import com.example.test.entity.User;
import com.example.test.repository.ProductRepository;
import com.example.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner
{
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

            Product product3 = new Product();
            product3.setName("Hat");
            product3.setPrice(55.12);
            product3.setQuantity(20);

            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);

            System.out.println("Product initialized");
        }

        if (userRepository.count() == 0)
        {

            User user2 = new User();
            user2.setUsername("adminuser");
            user2.setPassword(passwordEncoder.encode("1234"));
            user2.setRole(Roles.ADMIN);

            userRepository.save(user2);

        }

    }
}
