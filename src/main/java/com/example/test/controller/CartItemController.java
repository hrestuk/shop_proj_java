package com.example.test.controller;

import com.example.test.entity.CartItem;
import com.example.test.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart-items")
public class CartItemController
{

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService)
    {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public List<CartItem> getAll() {
        return cartItemService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getById(@PathVariable Long id)
    {
        return cartItemService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CartItem create(@RequestBody CartItem cartItem)
    {
        return cartItemService.create(cartItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItem> update(@PathVariable Long id, @RequestBody CartItem cartItem)
    {
        CartItem updated = cartItemService.update(id, cartItem);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        cartItemService.delete(id);
    }
}
