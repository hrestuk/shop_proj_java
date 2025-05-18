package com.example.test.service;

import com.example.test.entity.Product;
import com.example.test.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest
{
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    public void setup()
    {
        sampleProduct = Product.builder().id(1L).name("TestProduct").price(100).quantity(10).build();
    }

    @Test
    public void createProduct_ShouldReturnSavedProduct()
    {
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);

        Product result = productService.create(sampleProduct);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("TestProduct");
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    public void getAll_ShouldReturnListOfProducts()
    {
        List<Product> products = Arrays.asList(sampleProduct);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAll();

        assertThat(result).hasSize(1);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void getById_ShouldReturnProduct()
    {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        Optional<Product> result = productService.getById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("TestProduct");
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void getById_ShouldReturnEmpty_WhenProductNotFound()
    {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getById(999L);

        assertThat(result).isEmpty();
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    public void updateProduct_ShouldReturnUpdatedProduct()
    {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        Product update = Product.builder().name("UpdatedName").price(200).quantity(5).build();

        Product result = productService.update(1L, update);

        assertThat(result.getName()).isEqualTo("UpdatedName");
        assertThat(result.getPrice()).isEqualTo(200);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void update_ShouldReturnNull_WhenProductNotFound()
    {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Product updated = productService.update(999L, sampleProduct);

        assertThat(updated).isNull();
        verify(productRepository, times(1)).findById(999L);
        verify(productRepository, never()).save(any());
    }

    @Test
    public void deleteProduct_ShouldCallRepositoryDelete()
    {
        productService.delete(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
}
