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
        // check if user already has this product in cart
        CartItem existingCartItem = cartRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId());
        if (existingCartItem != null) {
            // just bump up the quantity instead of creating new entry
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            return cartRepository.save(existingCartItem);
        }

        // new item for cart
        CartItem newCartItem = new CartItem();
        newCartItem.setUserId(request.getUserId());
        newCartItem.setProductId(request.getProductId());
        newCartItem.setQuantity(request.getQuantity());
        return cartRepository.save(newCartItem);
    }

    public List<CartItemResponse> getCartByUserId(String userId) {
        List<CartItem> userCartItems = cartRepository.findByUserId(userId);
        List<CartItemResponse> cartResponseList = new ArrayList<>();
        
        // TODO: might want to batch fetch products for better performance if cart gets huge
        for (CartItem cartItem : userCartItems) {
            CartItemResponse itemResp = new CartItemResponse();
            itemResp.setId(cartItem.getId());
            itemResp.setProductId(cartItem.getProductId());
            itemResp.setQuantity(cartItem.getQuantity());
            
            // grab product details to show in cart
            Product prod = productRepository.findById(cartItem.getProductId()).orElse(null);
            itemResp.setProduct(prod);
            cartResponseList.add(itemResp);
        }
        return cartResponseList;
    }

    /* 
     * Internal method - used by OrderService 
     * Returns raw cart items without product details 
     */
    public List<CartItem> getCartItemsByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }

    // wipe out entire cart - usually after order is created
    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
}
