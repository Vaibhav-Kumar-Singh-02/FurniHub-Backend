package com.furnihub.service;

import com.furnihub.dto.InventoryResponse;
import com.furnihub.entity.Category;
import com.furnihub.entity.Product;
import com.furnihub.repository.CategoryRepository;
import com.furnihub.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminInventoryServiceImpl implements AdminInventoryService {

    private static final Logger logger = LoggerFactory.getLogger(AdminInventoryServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public AdminInventoryServiceImpl(ProductRepository productRepository,
                                        CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<InventoryResponse> getInventory(String search, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (search != null && !search.isBlank()) {
            return productRepository.findByNameContainingIgnoreCase(search, pageable)
                    .map(this::mapToResponse);
        }

        if (category != null && !category.isBlank()) {
            return productRepository.findByCategorieId(Integer.parseInt(category), pageable)
                    .map(this::mapToResponse);
        }

        return productRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public void increaseStock(Integer productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        product.setStock(product.getStock() + quantity);
        productRepository.save(product);

        logger.info("Stock increased for product id: {} by quantity: {}", productId, quantity);
    }

    @Override
    @Transactional
    public void decreaseStock(Integer productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product id: " + productId);
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        logger.info("Stock decreased for product id: {} by quantity: {}", productId, quantity);
    }

    @Override
    public List<InventoryResponse> getLowStockProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .filter(p -> p.getStock() > 0 && p.getStock() <= 10)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getOutOfStockProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .filter(p -> p.getStock() <= 0)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private InventoryResponse mapToResponse(Product product) {
        Category category = categoryRepository.findById(product.getCategorieId()).orElse(null);

        return new InventoryResponse(
                product.getProductId(),
                product.getName(),
                product.getStock(),
                category != null ? category.getCategoryName() : null,
                product.getStatus().name(),
                product.getStock() > 0 && product.getStock() <= 10
        );
    }
}