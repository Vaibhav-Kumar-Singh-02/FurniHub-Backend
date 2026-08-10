package com.furnihub.service;

import com.furnihub.dto.CouponRequest;
import com.furnihub.dto.CouponResponse;
import com.furnihub.entity.Coupon;
import com.furnihub.repository.CouponRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminCouponServiceImpl implements AdminCouponService {

    private static final Logger logger = LoggerFactory.getLogger(AdminCouponServiceImpl.class);

    private final CouponRepository couponRepository;

    public AdminCouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        Coupon coupon = new Coupon();
        coupon.setCode(request.getCode());
        coupon.setDiscountType(Coupon.DiscountType.valueOf(request.getDiscountType()));
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setMaxDiscountAmount(request.getMaxDiscountAmount());
        coupon.setUsageLimit(request.getUsageLimit());
        coupon.setValidFrom(request.getValidFrom());
        coupon.setValidUntil(request.getValidUntil());
        coupon.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        coupon.setAppliesTo(request.getAppliesTo() != null ? request.getAppliesTo() : "ALL");
        coupon.setProductIds(request.getProductIds());

        coupon = couponRepository.save(coupon);

        logger.info("Coupon created successfully with id: {}", coupon.getCouponId());
        return mapToResponse(coupon);
    }

    @Override
    @Transactional
    public CouponResponse updateCoupon(Integer id, CouponRequest request) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));

        coupon.setCode(request.getCode());
        coupon.setDiscountType(Coupon.DiscountType.valueOf(request.getDiscountType()));
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setMaxDiscountAmount(request.getMaxDiscountAmount());
        coupon.setUsageLimit(request.getUsageLimit());
        coupon.setValidFrom(request.getValidFrom());
        coupon.setValidUntil(request.getValidUntil());
        coupon.setIsActive(request.getIsActive() != null ? request.getIsActive() : coupon.getIsActive());
        coupon.setAppliesTo(request.getAppliesTo() != null ? request.getAppliesTo() : coupon.getAppliesTo());
        coupon.setProductIds(request.getProductIds() != null ? request.getProductIds() : coupon.getProductIds());

        coupon = couponRepository.save(coupon);

        logger.info("Coupon updated successfully with id: {}", id);
        return mapToResponse(coupon);
    }

    @Override
    @Transactional
    public void disableCoupon(Integer id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));

        coupon.setIsActive(false);
        couponRepository.save(coupon);

        logger.info("Coupon disabled successfully with id: {}", id);
    }

    @Override
    @Transactional
    public void enableCoupon(Integer id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));

        coupon.setIsActive(true);
        couponRepository.save(coupon);

        logger.info("Coupon enabled successfully with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteCoupon(Integer id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));

        couponRepository.delete(coupon);
        logger.info("Coupon deleted successfully with id: {}", id);
    }

    @Override
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CouponResponse mapToResponse(Coupon coupon) {
        return new CouponResponse(
                coupon.getCouponId(),
                coupon.getCode(),
                coupon.getDiscountType().name(),
                coupon.getDiscountValue(),
                coupon.getMaxDiscountAmount(),
                coupon.getUsageLimit(),
                coupon.getUsedCount(),
                coupon.getIsActive(),
                coupon.getValidFrom(),
                coupon.getValidUntil(),
                coupon.getCreatedAt(),
                coupon.getUpdatedAt(),
                coupon.getAppliesTo(),
                coupon.getProductIds()
        );
    }
}