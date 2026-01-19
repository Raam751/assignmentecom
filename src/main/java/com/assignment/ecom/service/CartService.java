package com.assignment.ecom.service;

import com.assignment.ecom.dto.AddToCartRequest;
import com.assignment.ecom.dto.CartItemResponse;
import com.assignment.ecom.model.CartItem;
import com.assignment.ecom.model.Product;
import com.assignment.ecom.repository.CartRepository;
import com.assignment.ecom.repository.ProductRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartItem addToCart(AddToCartRequest request) {
        CartItem existing = cartRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + request.getQuantity());
            return cartRepository.save(existing);
        }
        CartItem cartItem = new CartItem();
        cartItem.setUserId(request.getUserId());
        cartItem.setProductId(request.getProductId());
        cartItem.setQuantity(request.getQuantity());
        return cartRepository.save(cartItem);
    }

    public List<CartItemResponse> getCartByUserId(String userId) {
        List<CartItem> items = cartRepository.findByUserId(userId);
        List<CartItemResponse> response = new ArrayList<>();
        for (CartItem item : items) {
            CartItemResponse r = new CartItemResponse();
            r.setId(item.getId());
            r.setProductId(item.getProductId());
            r.setQuantity(item.getQuantity());
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            r.setProduct(product);
            response.add(r);
        }
        return response;
    }

    public List<CartItem> getCartItemsByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
}
