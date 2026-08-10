package com.furnihub.service;

import com.furnihub.dto.InventoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminInventoryService {

    Page<InventoryResponse> getInventory(String search, String category, int page, int size);

    void increaseStock(Integer productId, Integer quantity);

    void decreaseStock(Integer productId, Integer quantity);

    List<InventoryResponse> getLowStockProducts();

    List<InventoryResponse> getOutOfStockProducts();
}