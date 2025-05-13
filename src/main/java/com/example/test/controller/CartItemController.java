package com.example.test.controller;

import com.example.test.entity.CartItem;
import com.example.test.entity.Order;
import com.example.test.entity.User;
import com.example.test.repository.UserRepository;
import com.example.test.service.CartItemService;
import com.example.test.service.OrderService;
import com.example.test.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cart-items")
public class CartItemController extends BaseController
{
    private final CartItemService cartItemService;
    private final OrderService orderService;
    private final UserRepository userRepository;

    public CartItemController(CartItemService cartItemService, OrderService orderService, UserRepository userRepository, UserService userService)
    {
        super(userService);
        this.cartItemService = cartItemService;
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<CartItem> getAll()
    {
        if (isAdmin())
        {
            return cartItemService.getAll();
        }

        return cartItemService.getByUsername(getCurrentUser().getUsername());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getById(@PathVariable Long id)
    {
        return cartItemService.getById(id)
                .filter(item -> isOwner(item.getUser()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CartItem create(@RequestBody CartItem cartItem)
    {
        User user = getCurrentUser();
        String username = user.getUsername();
        if (username == null) {
            throw new IllegalArgumentException("User not found");
        }

        cartItem.setUser(user);

        return cartItemService.create(cartItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItem> update(@PathVariable Long id, @RequestBody CartItem cartItem)
    {
        return cartItemService.getById(id)
                .filter(existing -> isOwner(existing.getUser()))
                .map(existing -> ResponseEntity.ok(cartItemService.update(id, cartItem)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        return cartItemService.getById(id)
                .filter(existing -> isAdmin() || isOwner(existing.getUser()))
                .map(existing -> {
                    cartItemService.delete(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Метод для оформлення замовлення
    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout()
    {
        try
        {
            Order order = orderService.checkout(getCurrentUser().getUsername());
            return ResponseEntity.ok(order);
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
