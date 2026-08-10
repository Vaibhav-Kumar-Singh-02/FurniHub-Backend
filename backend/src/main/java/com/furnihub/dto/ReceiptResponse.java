package com.furnihub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptResponse {
    private String orderId;
    private String customerName;
    private String customerEmail;
    private String customerMobile;
    private String shippingAddress;
    private String paymentMethod;
    private BigDecimal subtotal;
    private BigDecimal cgst;
    private BigDecimal sgst;
    private BigDecimal igst;
    private BigDecimal gstTotal;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime orderDate;
    private String deliveryDate;
    private List<ReceiptItemResponse> items;
}
