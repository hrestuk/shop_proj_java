package com.example.test.service;

import com.example.test.entity.Product;
import com.example.test.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductServiceTest
{
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void SaveProductTest()
    {
        Product product = new Product();
        product.setName("TestProduct1");
        product.setPrice(123);
        product.setQuantity(321);

        Product saved = productRepository.save(product);

        Assertions.assertThat(saved.getId()).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void FindProductByIdTest()
    {
        Product product = new Product();
        product.setName("FindTest");
        product.setPrice(100);
        product.setQuantity(10);

        Product saved = productRepository.save(product);

        Product found = productRepository.findById(saved.getId()).orElse(null);

        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getName()).isEqualTo("FindTest");
    }

    @Test
    public void UpdateProductTest()
    {
        Product product = new Product();
        product.setName("OldName");
        product.setPrice(50);
        product.setQuantity(5);

        Product saved = productRepository.save(product);
        saved.setName("UpdatedName");

        Product updated = productRepository.save(saved);

        Assertions.assertThat(updated.getName()).isEqualTo("UpdatedName");
    }

    @Test
    public void DeleteProductTest()
    {
        Product product = new Product();
        product.setName("ToDelete");
        product.setPrice(10);
        product.setQuantity(1);

        Product saved = productRepository.save(product);
        Assertions.assertThat(saved.getId()).isNotNull();
        productRepository.delete(saved);

        boolean exists = productRepository.findById(saved.getId()).isPresent();
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    public void FindAllProductsTest()
    {
        Product p1 = new Product();
        p1.setName("Prod1");
        p1.setPrice(100);
        p1.setQuantity(1);

        Product p2 = new Product();
        p2.setName("Prod2");
        p2.setPrice(200);
        p2.setQuantity(2);

        productRepository.save(p1);
        productRepository.save(p2);

        List<Product> products = productRepository.findAll();

        Assertions.assertThat(products).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    public void FindByNameTest()
    {
        Product product = new Product();
        product.setName("UniqueName");
        product.setPrice(999);
        product.setQuantity(9);

        productRepository.save(product);

        Product found = productRepository.findByName("UniqueName");

        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getName()).isEqualTo("UniqueName");
    }

    @Test
    public void NegativeIdTest()
    {
        Product product = new Product();
        product.setName("TestProduct");
        product.setPrice(100);
        product.setQuantity(10);

        Product saved = productRepository.save(product);

        // неправильна перевірка: id не може бути -1
        Assertions.assertThat(saved.getId()).isEqualTo(-1);
    }

    @Test
    public void EmptyProductTest()
    {
        Product product = new Product();
        product.setName("TestProduct");
        product.setPrice(100);
        product.setQuantity(10);

        Product saved = productRepository.save(product);

        // saved точно не null
        Assertions.assertThat(saved).isNull();
    }

}
