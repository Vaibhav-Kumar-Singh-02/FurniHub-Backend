package com.furnihub.repository;

import com.furnihub.entity.OtpVerification;
import com.furnihub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Integer> {
    Optional<OtpVerification> findByUserAndOtpAndVerifiedFalse(User user, String otp);
    Optional<OtpVerification> findByUserAndOtpAndVerifiedTrue(User user, String otp);
    void deleteByUser(User user);
}
