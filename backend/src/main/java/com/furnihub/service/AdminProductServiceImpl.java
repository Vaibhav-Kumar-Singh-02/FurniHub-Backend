package com.furnihub.service;

import com.furnihub.dto.ProductRequest;
import com.furnihub.dto.ProductResponse;
import com.furnihub.entity.Category;
import com.furnihub.entity.Product;
import com.furnihub.entity.ProductImage;
import com.furnihub.repository.CategoryRepository;
import com.furnihub.repository.ProductImageRepository;
import com.furnihub.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminProductServiceImpl implements AdminProductService {

    private static final Logger logger = LoggerFactory.getLogger(AdminProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    public AdminProductServiceImpl(ProductRepository productRepository,
                                    CategoryRepository categoryRepository,
                                    ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    @Transactional
    public ProductResponse addProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setCategorieId(request.getCategoryId());
        product.setSubcategory(request.getSubcategory());
        product.setDescription(request.getDescription());
        product.setIngredients(request.getIngredients());
        product.setBenefits(request.getBenefits());
        product.setHowToUse(request.getHowToUse());
        product.setFurnitureType(request.getFurnitureType());
        product.setProductSize(request.getProductSize());
        product.setPrice(request.getPrice());
        product.setDiscount(request.getDiscount());
        product.setStock(request.getStock());
        product.setStatus(Product.ProductStatus.valueOf(request.getStatus()));

        product = productRepository.save(product);

        if (request.getImageUrls() != null) {
            for (String imageUrl : request.getImageUrls()) {
                ProductImage image = new ProductImage();
                image.setProductId(product.getProductId());
                image.setImageUrl(imageUrl);
                productImageRepository.save(image);
            }
        }

        logger.info("Product added successfully with id: {}", product.getProductId());
        return mapToResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Integer id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setCategorieId(request.getCategoryId());
        product.setSubcategory(request.getSubcategory());
        product.setDescription(request.getDescription());
        product.setIngredients(request.getIngredients());
        product.setBenefits(request.getBenefits());
        product.setHowToUse(request.getHowToUse());
        product.setFurnitureType(request.getFurnitureType());
        product.setProductSize(request.getProductSize());
        product.setPrice(request.getPrice());
        product.setDiscount(request.getDiscount());
        product.setStock(request.getStock());
        product.setStatus(Product.ProductStatus.valueOf(request.getStatus()));

        product = productRepository.save(product);

        logger.info("Product updated successfully with id: {}", id);
        return mapToResponse(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        List<ProductImage> images = productImageRepository.findByProductId(id);
        if (!images.isEmpty()) {
            productImageRepository.deleteAll(images);
        }

        productRepository.delete(product);
        logger.info("Product deleted successfully with id: {}", id);
    }

    @Override
    public Page<ProductResponse> getAllProducts(String search, String category, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (search != null && !search.isBlank()) {
            return productRepository.findByNameContainingIgnoreCase(search, pageable)
                    .map(this::mapToResponse);
        }

        if (category != null && !category.isBlank()) {
            return productRepository.findByCategorieId(Integer.parseInt(category), pageable)
                    .map(this::mapToResponse);
        }

        if (status != null && !status.isBlank()) {
            return productRepository.findByStatus(Product.ProductStatus.valueOf(status), pageable)
                    .map(this::mapToResponse);
        }

        return productRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        List<ProductResponse> response = new ArrayList<>();
        for (Product product : products) {
            response.add(mapToResponse(product));
        }
        return response;
    }

    private ProductResponse mapToResponse(Product product) {
        Category category = categoryRepository.findById(product.getCategorieId()).orElse(null);
        List<ProductImage> images = productImageRepository.findByProductId(product.getProductId());
        List<String> imageUrls = images.stream().map(ProductImage::getImageUrl).toList();

        ProductResponse response = new ProductResponse();
        response.setProductId(product.getProductId());
        response.setName(product.getName());
        response.setBrand(product.getBrand());
        response.setCategoryName(category != null ? category.getCategoryName() : null);
        response.setSubcategory(product.getSubcategory());
        response.setDescription(product.getDescription());
        response.setIngredients(product.getIngredients());
        response.setBenefits(product.getBenefits());
        response.setHowToUse(product.getHowToUse());
        response.setFurnitureType(product.getFurnitureType());
        response.setProductSize(product.getProductSize());
        response.setPrice(product.getPrice());
        response.setDiscount(product.getDiscount());
        response.setStock(product.getStock());
        response.setRatings(product.getRatings());
        response.setStatus(product.getStatus().name());
        response.setImageUrls(imageUrls);
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
}