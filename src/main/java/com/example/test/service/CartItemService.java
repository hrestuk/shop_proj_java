package com.example.test.service;

import com.example.test.entity.CartItem;
import com.example.test.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService
{

    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository)
    {
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItem> getAll()
    {
        return cartItemRepository.findAll();
    }

    public Optional<CartItem> getById(Long id)
    {
        return cartItemRepository.findById(id);
    }
    public List<CartItem> getByUsername(String username)
    {
        return cartItemRepository.findByUser_Username(username);
    }

    public CartItem create(CartItem cartItem)
    {
        return cartItemRepository.save(cartItem);
    }

    public CartItem update(Long id, CartItem cartItem)
    {
        return cartItemRepository.findById(id).map(existing -> {
            existing.setQuantity(cartItem.getQuantity());
            return cartItemRepository.save(existing);
        }).orElse(null);
    }

    public void delete(Long id)
    {
        cartItemRepository.deleteById(id);
    }

}
