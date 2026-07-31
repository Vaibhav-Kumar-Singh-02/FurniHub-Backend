package com.furnihub.backend.controller;

import com.furnihub.backend.dto.AddCartItemRequest;
import com.furnihub.backend.dto.CartDto;
import com.furnihub.backend.dto.CartItemDto;
import com.furnihub.backend.dto.UpdateCartItemRequest;
import com.furnihub.backend.entity.User;
import com.furnihub.backend.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public CartDto getCart(Authentication authentication) {
        return cartService.getCart(currentUserId(authentication));
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemDto> addItem(@Valid @RequestBody AddCartItemRequest request,
                                               Authentication authentication) {
        User user = currentUser(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(user, request));
    }

    @PutMapping("/items/{id}")
    public CartItemDto updateItem(@PathVariable Long id,
                                  @Valid @RequestBody UpdateCartItemRequest request,
                                  Authentication authentication) {
        return cartService.updateItem(currentUserId(authentication), id, request);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeItem(@PathVariable Long id, Authentication authentication) {
        cartService.removeItem(currentUserId(authentication), id);
        return ResponseEntity.noContent().build();
    }

    private User currentUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    private Long currentUserId(Authentication authentication) {
        return currentUser(authentication).getId();
    }
}
