package com.furnihub.controller;

import com.furnihub.dto.CouponRequest;
import com.furnihub.dto.CouponResponse;
import com.furnihub.service.AdminCouponService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/coupons")
public class AdminCouponController {

    private final AdminCouponService adminCouponService;

    public AdminCouponController(AdminCouponService adminCouponService) {
        this.adminCouponService = adminCouponService;
    }

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CouponRequest request) {
        CouponResponse response = adminCouponService.createCoupon(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponResponse> updateCoupon(@PathVariable Integer id, @Valid @RequestBody CouponRequest request) {
        CouponResponse response = adminCouponService.updateCoupon(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/disable")
    public ResponseEntity<Void> disableCoupon(@PathVariable Integer id) {
        adminCouponService.disableCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/enable")
    public ResponseEntity<Void> enableCoupon(@PathVariable Integer id) {
        adminCouponService.enableCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Integer id) {
        adminCouponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> getAllCoupons() {
        List<CouponResponse> response = adminCouponService.getAllCoupons();
        return ResponseEntity.ok(response);
    }
}
