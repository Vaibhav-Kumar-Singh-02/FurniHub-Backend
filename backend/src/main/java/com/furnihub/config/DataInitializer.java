package com.furnihub.config;

import com.furnihub.entity.Category;
import com.furnihub.entity.Product;
import com.furnihub.entity.ProductImage;
import com.furnihub.repository.CategoryRepository;
import com.furnihub.repository.ProductImageRepository;
import com.furnihub.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public DataInitializer(CategoryRepository categoryRepository,
                          ProductRepository productRepository,
                          ProductImageRepository productImageRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    public void run(String... args) {
        // Data auto-seeding is managed cleanly by data.sql
        return;
    }

    private Category saveCategory(String name) {
        Category category = new Category();
        category.setCategoryName(name);
        return categoryRepository.save(category);
    }

    private void saveProduct(String name, String brand, String description, BigDecimal price, int stock, Integer categorieId, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setBrand(brand);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategorieId(categorieId);
        product = productRepository.save(product);

        ProductImage image = new ProductImage();
        image.setProductId(product.getProductId());
        image.setImageUrl(imageUrl);
        productImageRepository.save(image);
    }
}
