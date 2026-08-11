package com.furnihub.service;

import com.furnihub.dto.WishlistResponse;
import com.furnihub.entity.Category;
import com.furnihub.entity.Product;
import com.furnihub.entity.ProductImage;
import com.furnihub.entity.User;
import com.furnihub.entity.Wishlist;
import com.furnihub.repository.CategoryRepository;
import com.furnihub.repository.ProductImageRepository;
import com.furnihub.repository.ProductRepository;
import com.furnihub.repository.UserRepository;
import com.furnihub.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository,
                               UserRepository userRepository,
                               ProductRepository productRepository,
                               ProductImageRepository productImageRepository,
                               CategoryRepository categoryRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<WishlistResponse> getWishlist(Integer userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUser_UserIdOrderByCreatedAtDesc(userId);
        return wishlists.stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional
    public WishlistResponse addToWishlist(Integer userId, Integer productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<Wishlist> existing = wishlistRepository.findByUser_UserIdAndProduct_ProductId(userId, productId);
        if (existing.isPresent()) {
            return mapToResponse(existing.get());
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlist = wishlistRepository.save(wishlist);
        return mapToResponse(wishlist);
    }

    @Override
    @Transactional
    public void removeFromWishlist(Integer userId, Integer productId) {
        wishlistRepository.deleteByUser_UserIdAndProduct_ProductId(userId, productId);
    }

    @Override
    public boolean isInWishlist(Integer userId, Integer productId) {
        return wishlistRepository.existsByUser_UserIdAndProduct_ProductId(userId, productId);
    }

    private WishlistResponse mapToResponse(Wishlist wishlist) {
        Product product = wishlist.getProduct();
        List<ProductImage> images = productImageRepository.findByProductId(product.getProductId());
        String imageUrl = images.isEmpty() ? null : images.get(0).getImageUrl();

        Category category = categoryRepository.findById(product.getCategorieId()).orElse(null);

        WishlistResponse response = new WishlistResponse();
        response.setId(wishlist.getId());
        response.setUserId(wishlist.getUser().getUserId());
        response.setProductId(product.getProductId());
        response.setProductName(product.getName());
        response.setBrand(product.getBrand());
        response.setPrice(product.getPrice());
        response.setDiscount(product.getDiscount());
        response.setStock(product.getStock());
        response.setCategoryName(category != null ? category.getCategoryName() : null);
        response.setImageUrls(imageUrl != null ? List.of(imageUrl) : new ArrayList<>());
        response.setCreatedAt(wishlist.getCreatedAt());
        return response;
    }
}
