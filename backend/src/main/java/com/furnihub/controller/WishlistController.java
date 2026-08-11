package com.furnihub.controller;

import com.furnihub.dto.WishlistResponse;
import com.furnihub.entity.User;
import com.furnihub.repository.UserRepository;
import com.furnihub.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserRepository userRepository;

    public WishlistController(WishlistService wishlistService, UserRepository userRepository) {
        this.wishlistService = wishlistService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getWishlist(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(wishlistService.getWishlist(user.getUserId()));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<?> addToWishlist(Authentication authentication,
                                            @PathVariable Integer productId) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            WishlistResponse response = wishlistService.addToWishlist(user.getUserId(), productId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add to wishlist: " + e.getMessage());
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFromWishlist(Authentication authentication,
                                                 @PathVariable Integer productId) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        wishlistService.removeFromWishlist(user.getUserId(), productId);
        return ResponseEntity.ok("Removed from wishlist");
    }

    @GetMapping("/check/{productId}")
    public ResponseEntity<Boolean> checkWishlist(Authentication authentication,
                                                  @PathVariable Integer productId) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(wishlistService.isInWishlist(user.getUserId(), productId));
    }
}
