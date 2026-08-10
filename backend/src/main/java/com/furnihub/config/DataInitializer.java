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
        if (categoryRepository.count() > 0) {
            return;
        }

        // Seed categories
        Category livingRoom = saveCategory("Living Room");
        Category bedroom = saveCategory("Bedroom");
        Category dining = saveCategory("Dining");

        // Seed products with images
        saveProduct("Premium Sofa Set", "Elegant 3-seater sofa with plush cushions and solid wood frame", new BigDecimal("35999"), 15, livingRoom.getCategorieId(),
                "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=400&q=80");
        saveProduct("Accent Chair", "Modern accent chair with ergonomic design and fabric upholstery", new BigDecimal("12999"), 25, livingRoom.getCategorieId(),
                "https://images.unsplash.com/photo-1519947486511-46149fa0a254?w=400&q=80");
        saveProduct("Coffee Table", "Sleek glass-top coffee table with metal legs", new BigDecimal("8999"), 30, livingRoom.getCategorieId(),
                "https://images.unsplash.com/photo-1532372576444-dda953c42b36?w=400&q=80");

        saveProduct("Queen Size Bed", "Solid wood queen bed with storage and headboard", new BigDecimal("49999"), 10, bedroom.getCategorieId(),
                "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=400&q=80");
        saveProduct("Wardrobe", "Spacious 3-door wardrobe with mirror and multiple compartments", new BigDecimal("45999"), 8, bedroom.getCategorieId(),
                "https://images.unsplash.com/photo-1558997519-83ea9252edf8?w=400&q=80");
        saveProduct("Bedside Table", "Compact bedside table with drawer and open shelf", new BigDecimal("6999"), 40, bedroom.getCategorieId(),
                "https://images.unsplash.com/photo-1532372576444-dda953c42b36?w=400&q=80");

        saveProduct("Dining Table Set", "6-seater wooden dining table with chairs", new BigDecimal("28999"), 12, dining.getCategorieId(),
                "https://images.unsplash.com/photo-1484101403633-562f891dc89a?w=400&q=80");
        saveProduct("Dining Chair Set (4)", "Set of 4 upholstered dining chairs with cushioned seats", new BigDecimal("15999"), 20, dining.getCategorieId(),
                "https://images.unsplash.com/photo-1503602642458-232111445657?w=400&q=80");

        System.out.println("=== Seed data loaded: 3 categories, 8 products ===");
    }

    private Category saveCategory(String name) {
        Category category = new Category();
        category.setCategoryName(name);
        return categoryRepository.save(category);
    }

    private void saveProduct(String name, String description, BigDecimal price, int stock, Integer categorieId, String imageUrl) {
        Product product = new Product();
        product.setName(name);
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
