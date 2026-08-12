package com.furnihub.controller;

import com.furnihub.config.JwtUtil;
import com.furnihub.dto.ProductResponse;
import com.furnihub.service.CatalogService;
import com.furnihub.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CatalogController.class)
@AutoConfigureMockMvc(addFilters = false)
class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatalogService catalogService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldReturnProductsFromCatalogService() throws Exception {
        ProductResponse product = new ProductResponse();
        product.setProductId(1);
        product.setName("Modern Sofa");
        product.setDescription("Comfortable sofa");
        product.setPrice(BigDecimal.valueOf(29999));
        product.setStock(8);
        product.setCategoryName("Living Room");
        when(catalogService.getProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Modern Sofa"));
    }
}
