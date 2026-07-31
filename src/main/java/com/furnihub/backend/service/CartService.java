package com.furnihub.backend.service;

import com.furnihub.backend.dto.AddCartItemRequest;
import com.furnihub.backend.dto.CartDto;
import com.furnihub.backend.dto.CartItemDto;
import com.furnihub.backend.dto.UpdateCartItemRequest;
import com.furnihub.backend.entity.CartItem;
import com.furnihub.backend.entity.Product;
import com.furnihub.backend.entity.User;
import com.furnihub.backend.exception.ResourceNotFoundException;
import com.furnihub.backend.repository.CartItemRepository;
import com.furnihub.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public CartDto getCart(Long userId) {
        List<CartItem> items = cartItemRepository.findByUserIdOrderByCreatedAtAsc(userId);
        return CartDto.from(items);
    }

    @Transactional
    public CartItemDto addItem(User user, AddCartItemRequest request) {
        Product product = productRepository.findByIdAndActiveTrue(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItem existing = cartItemRepository
                .findByUserIdAndProductId(user.getId(), product.getId())
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + request.quantity());
            return CartItemDto.from(cartItemRepository.save(existing));
        }

        CartItem item = new CartItem();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(request.quantity());
        return CartItemDto.from(cartItemRepository.save(item));
    }

    @Transactional
    public CartItemDto updateItem(Long userId, Long itemId, UpdateCartItemRequest request) {
        CartItem item = cartItemRepository.findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        item.setQuantity(request.quantity());
        return CartItemDto.from(cartItemRepository.save(item));
    }

    @Transactional
    public void removeItem(Long userId, Long itemId) {
        CartItem item = cartItemRepository.findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        cartItemRepository.delete(item);
    }
}
