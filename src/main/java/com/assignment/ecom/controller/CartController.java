package com.assignment.ecom.controller;

import com.assignment.ecom.dto.AddToCartRequest;
import com.assignment.ecom.dto.CartItemResponse;
import com.assignment.ecom.model.CartItem;
import com.assignment.ecom.service.CartService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public CartItem addToCart(@RequestBody AddToCartRequest request) {
        return cartService.addToCart(request);
    }

    @GetMapping("/{userId}")
    public List<CartItemResponse> getCart(@PathVariable String userId) {
        return cartService.getCartByUserId(userId);
    }

    @DeleteMapping("/{userId}/clear")
    public Map<String, String> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cart cleared successfully");
        return response;
    }
}
