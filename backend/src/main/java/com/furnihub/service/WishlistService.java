package com.furnihub.service;

import com.furnihub.dto.WishlistResponse;
import java.util.List;

public interface WishlistService {
    List<WishlistResponse> getWishlist(Integer userId);
    WishlistResponse addToWishlist(Integer userId, Integer productId);
    void removeFromWishlist(Integer userId, Integer productId);
    boolean isInWishlist(Integer userId, Integer productId);
}
