package com.furnihub.service;

import com.furnihub.dto.CouponRequest;
import com.furnihub.dto.CouponResponse;

import java.util.List;

public interface AdminCouponService {

    CouponResponse createCoupon(CouponRequest request);

    CouponResponse updateCoupon(Integer id, CouponRequest request);

    void disableCoupon(Integer id);

    void enableCoupon(Integer id);

    void deleteCoupon(Integer id);

    List<CouponResponse> getAllCoupons();
}