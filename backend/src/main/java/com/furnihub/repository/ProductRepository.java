package com.furnihub.repository;

import com.furnihub.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByCategorieId(Integer categorieId, Pageable pageable);
    Page<Product> findByStatus(Product.ProductStatus status, Pageable pageable);
    java.util.List<Product> findByNameContainingIgnoreCase(String name);
}
