package com.furnihub.service;

import com.furnihub.dto.CategoryResponse;
import com.furnihub.dto.CouponResponse;
import com.furnihub.dto.CouponValidateResponse;
import com.furnihub.dto.ProductResponse;
import com.furnihub.entity.Category;
import com.furnihub.entity.Coupon;
import com.furnihub.entity.Product;
import com.furnihub.entity.ProductImage;
import com.furnihub.repository.CategoryRepository;
import com.furnihub.repository.CouponRepository;
import com.furnihub.repository.ProductImageRepository;
import com.furnihub.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class CatalogService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final CouponRepository couponRepository;

    public CatalogService(ProductRepository productRepository, CategoryRepository categoryRepository,
                          ProductImageRepository productImageRepository, CouponRepository couponRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
        this.couponRepository = couponRepository;
    }

    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
            .map(category -> {
                CategoryResponse response = new CategoryResponse();
                response.setCategorieId(category.getCategorieId());
                response.setCategoryName(category.getCategoryName());
                return response;
            })
            .toList();
    }

    public List<ProductResponse> getProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> response = new ArrayList<>();

        for (Product product : products) {
            Category category = categoryRepository.findById(product.getCategorieId()).orElse(null);
            List<ProductImage> images = productImageRepository.findByProductId(product.getProductId());
            String imageUrl = images.isEmpty() ? null : images.get(0).getImageUrl();

            ProductResponse productResponse = new ProductResponse();
            productResponse.setProductId(product.getProductId());
            productResponse.setName(product.getName());
            productResponse.setDescription(product.getDescription());
            productResponse.setPrice(product.getPrice());
            productResponse.setStock(product.getStock());
            productResponse.setCategoryName(category != null ? category.getCategoryName() : null);
            productResponse.setImageUrls(imageUrl != null ? List.of(imageUrl) : List.of());
            response.add(productResponse);
        }

        return response;
    }

    public List<ProductResponse> searchProducts(String query) {
        String[] words = query.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", " ").split("\\s+");
        List<Product> products = productRepository.findAll();
        List<ProductResponse> response = new ArrayList<>();

        for (Product product : products) {
            Category category = categoryRepository.findById(product.getCategorieId()).orElse(null);
            String catName = category != null && category.getCategoryName() != null ? category.getCategoryName().toLowerCase() : "";
            
            boolean categoryMatch = true;
            for (String word : words) {
                if (!word.isBlank() && !catName.contains(word)) {
                    categoryMatch = false;
                    break;
                }
            }
            
            if (categoryMatch) {
                List<ProductImage> images = productImageRepository.findByProductId(product.getProductId());
                String imageUrl = images.isEmpty() ? null : images.get(0).getImageUrl();

                ProductResponse productResponse = new ProductResponse();
                productResponse.setProductId(product.getProductId());
                productResponse.setName(product.getName());
                productResponse.setDescription(product.getDescription());
                productResponse.setPrice(product.getPrice());
                productResponse.setStock(product.getStock());
                productResponse.setCategoryName(category != null ? category.getCategoryName() : null);
                productResponse.setImageUrls(imageUrl != null ? List.of(imageUrl) : List.of());
                response.add(productResponse);
            }
        }

        return response;
    }

    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        Category category = categoryRepository.findById(product.getCategorieId()).orElse(null);
        List<ProductImage> images = productImageRepository.findByProductId(product.getProductId());
        String imageUrl = images.isEmpty() ? null : images.get(0).getImageUrl();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductId(product.getProductId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setStock(product.getStock());
        productResponse.setCategoryName(category != null ? category.getCategoryName() : null);
        productResponse.setImageUrls(imageUrl != null ? List.of(imageUrl) : List.of());
        return productResponse;
    }

    public List<CouponResponse> getActiveCoupons() {
        LocalDateTime now = LocalDateTime.now();
        return couponRepository.findByIsActiveTrue().stream()
                .filter(coupon -> coupon.getValidFrom() == null || !now.isBefore(coupon.getValidFrom()))
                .filter(coupon -> coupon.getValidUntil() == null || !now.isAfter(coupon.getValidUntil()))
                .filter(coupon -> coupon.getUsageLimit() == null || coupon.getUsedCount() < coupon.getUsageLimit())
                .map(coupon -> new CouponResponse(
                        coupon.getCouponId(),
                        coupon.getCode(),
                        coupon.getDiscountType().name(),
                        coupon.getDiscountValue(),
                        coupon.getMaxDiscountAmount(),
                        coupon.getUsageLimit(),
                        coupon.getUsedCount(),
                        coupon.getIsActive(),
                        coupon.getValidFrom(),
                        coupon.getValidUntil(),
                        coupon.getCreatedAt(),
                        coupon.getUpdatedAt(),
                        coupon.getAppliesTo(),
                        coupon.getProductIds()
                ))
                .toList();
    }

    public CouponValidateResponse validateCoupon(String code, BigDecimal subtotal, List<Integer> productIds) {
        Optional<Coupon> optionalCoupon = couponRepository.findByCodeAndIsActiveTrue(code.toUpperCase());

        if (optionalCoupon.isEmpty()) {
            return new CouponValidateResponse(false, "Invalid coupon code", code, null, null, BigDecimal.ZERO, null, subtotal);
        }

        Coupon coupon = optionalCoupon.get();
        LocalDateTime now = LocalDateTime.now();

        if (coupon.getValidFrom() != null && now.isBefore(coupon.getValidFrom())) {
            return new CouponValidateResponse(false, "Coupon is not yet valid", code, null, null, BigDecimal.ZERO, null, subtotal);
        }

        if (coupon.getValidUntil() != null && now.isAfter(coupon.getValidUntil())) {
            return new CouponValidateResponse(false, "Coupon has expired", code, null, null, BigDecimal.ZERO, null, subtotal);
        }

        if (coupon.getUsageLimit() != null && coupon.getUsedCount() >= coupon.getUsageLimit()) {
            return new CouponValidateResponse(false, "Coupon usage limit reached", code, null, null, BigDecimal.ZERO, coupon.getMaxDiscountAmount(), subtotal);
        }

        if ("SPECIFIC".equalsIgnoreCase(coupon.getAppliesTo())) {
            if (coupon.getProductIds() == null || coupon.getProductIds().trim().isEmpty()) {
                return new CouponValidateResponse(false, "Coupon is not applicable to any products", code, null, null, BigDecimal.ZERO, null, subtotal);
            }
            List<Integer> allowedProductIds = Arrays.stream(coupon.getProductIds().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            if (productIds == null || productIds.isEmpty()) {
                return new CouponValidateResponse(false, "Cart is empty", code, null, null, BigDecimal.ZERO, null, subtotal);
            }
            boolean hasApplicableProduct = productIds.stream().anyMatch(allowedProductIds::contains);
            if (!hasApplicableProduct) {
                return new CouponValidateResponse(false, "Coupon is not applicable to products in your cart", code, null, null, BigDecimal.ZERO, null, subtotal);
            }
        }

        BigDecimal discountAmount = BigDecimal.ZERO;
        if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType().name())) {
            discountAmount = subtotal.multiply(coupon.getDiscountValue().divide(new BigDecimal("100")));
        } else {
            discountAmount = coupon.getDiscountValue();
        }

        if (coupon.getMaxDiscountAmount() != null && discountAmount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
            discountAmount = coupon.getMaxDiscountAmount();
        }

        BigDecimal newSubtotal = subtotal.subtract(discountAmount);
        if (newSubtotal.compareTo(BigDecimal.ZERO) < 0) {
            newSubtotal = BigDecimal.ZERO;
        }

        return new CouponValidateResponse(
                true,
                "Coupon applied successfully",
                coupon.getCode(),
                coupon.getDiscountType().name(),
                coupon.getDiscountValue(),
                discountAmount,
                coupon.getMaxDiscountAmount(),
                newSubtotal
        );
    }
}
