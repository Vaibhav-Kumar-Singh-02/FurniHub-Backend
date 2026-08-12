package com.furnihub.service;

import com.furnihub.dto.AdminSettingsRequest;
import com.furnihub.dto.UserResponse;
import com.furnihub.entity.Admin;
import com.furnihub.entity.AppSettings;
import com.furnihub.repository.AdminRepository;
import com.furnihub.repository.AppSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminSettingsServiceImpl implements AdminSettingsService {

    private static final Logger logger = LoggerFactory.getLogger(AdminSettingsServiceImpl.class);

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppSettingsRepository appSettingsRepository;

    public AdminSettingsServiceImpl(AdminRepository adminRepository,
                                    PasswordEncoder passwordEncoder,
                                    AppSettingsRepository appSettingsRepository) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.appSettingsRepository = appSettingsRepository;
    }

    @Override
    public UserResponse getAdminProfile(Integer adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));
        return mapAdminToResponse(admin);
    }

    @Override
    @Transactional
    public UserResponse updateAdminProfile(Integer adminId, AdminSettingsRequest request) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        if (request.getFullName() != null) {
            admin.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            admin.setEmail(request.getEmail());
        }
        if (request.getMobile() != null) {
            admin.setMobile(request.getMobile());
        }
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (request.getCurrentPassword() == null || !passwordEncoder.matches(request.getCurrentPassword(), admin.getPassword())) {
                throw new RuntimeException("Current password is incorrect");
            }
            if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                throw new RuntimeException("New passwords do not match");
            }
            admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        admin = adminRepository.save(admin);
        logger.info("Admin profile updated for id: {}", adminId);
        return mapAdminToResponse(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getAdminProfileByEmail(String email) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));
        return mapAdminToResponse(admin);
    }

    @Override
    @Transactional
    public UserResponse updateAdminProfileByEmail(String email, AdminSettingsRequest request) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));

        if (request.getFullName() != null) {
            admin.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            admin.setEmail(request.getEmail());
        }
        if (request.getMobile() != null) {
            admin.setMobile(request.getMobile());
        }

        admin = adminRepository.save(admin);
        logger.info("Admin profile updated for email: {}", email);
        return mapAdminToResponse(admin);
    }

    @Override
    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));

        if (!passwordEncoder.matches(currentPassword, admin.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        admin.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(admin);
        logger.info("Password changed for admin: {}", email);
    }

    @Override
    public Map<String, String> getAppSettings() {
        Optional<AppSettings> settingsOpt = appSettingsRepository.findFirstByOrderByIdAsc();
        if (settingsOpt.isPresent()) {
            AppSettings settings = settingsOpt.get();
            Map<String, String> map = new HashMap<>();
            map.put("siteName", settings.getSiteName() != null ? settings.getSiteName() : "FurniHub");
            map.put("siteDescription", settings.getSiteDescription() != null ? settings.getSiteDescription() : "Comfortable Living Spaces");
            map.put("supportEmail", settings.getSupportEmail() != null ? settings.getSupportEmail() : "support@furnihub.com");
            map.put("currency", settings.getCurrency() != null ? settings.getCurrency() : "INR");
            return map;
        }
        Map<String, String> defaults = new HashMap<>();
        defaults.put("siteName", "FurniHub");
        defaults.put("siteDescription", "Comfortable Living Spaces");
        defaults.put("supportEmail", "support@furnihub.com");
        defaults.put("currency", "INR");
        return defaults;
    }

    @Override
    @Transactional
    public Map<String, String> updateAppSettings(Map<String, String> settings) {
        Optional<AppSettings> settingsOpt = appSettingsRepository.findFirstByOrderByIdAsc();
        AppSettings appSettings = settingsOpt.orElse(new AppSettings());

        if (settings.containsKey("siteName")) {
            appSettings.setSiteName(settings.get("siteName"));
        }
        if (settings.containsKey("siteDescription")) {
            appSettings.setSiteDescription(settings.get("siteDescription"));
        }
        if (settings.containsKey("supportEmail")) {
            appSettings.setSupportEmail(settings.get("supportEmail"));
        }
        if (settings.containsKey("currency")) {
            appSettings.setCurrency(settings.get("currency"));
        }

        appSettingsRepository.save(appSettings);
        logger.info("Application settings updated");
        return Map.of("message", "Application settings updated successfully");
    }

    private UserResponse mapAdminToResponse(Admin admin) {
        return new UserResponse(
                admin.getAdminId(),
                admin.getUsername(),
                admin.getFullName(),
                admin.getEmail(),
                admin.getMobile(),
                admin.getRole().name(),
                admin.getIsActive(),
                admin.getCreatedAt()
        );
    }
}