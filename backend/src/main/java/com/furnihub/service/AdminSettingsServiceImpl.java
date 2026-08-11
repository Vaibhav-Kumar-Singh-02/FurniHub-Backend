package com.furnihub.service;

import com.furnihub.dto.AdminSettingsRequest;
import com.furnihub.dto.UserResponse;
import com.furnihub.entity.AppSettings;
import com.furnihub.entity.User;
import com.furnihub.repository.AppSettingsRepository;
import com.furnihub.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppSettingsRepository appSettingsRepository;

    public AdminSettingsServiceImpl(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    AppSettingsRepository appSettingsRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.appSettingsRepository = appSettingsRepository;
    }

    @Override
    public UserResponse getAdminProfile(Integer adminId) {
        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        return mapToResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateAdminProfile(Integer adminId, AdminSettingsRequest request) {
        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getMobile() != null) {
            user.setMobile(request.getMobile());
        }
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (request.getCurrentPassword() == null || !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new RuntimeException("Current password is incorrect");
            }
            if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                throw new RuntimeException("New passwords do not match");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        user = userRepository.save(user);

        logger.info("Admin profile updated for id: {}", adminId);
        return mapToResponse(user);
    }

    @Override
    public UserResponse getAdminProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));

        return mapToResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateAdminProfileByEmail(String email, AdminSettingsRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getMobile() != null) {
            user.setMobile(request.getMobile());
        }

        user = userRepository.save(user);

        logger.info("Admin profile updated for email: {}", email);
        return mapToResponse(user);
    }

    @Override
    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        logger.info("Password changed for admin: {}", email);
    }

    @Override
    public Map<String, String> getAppSettings() {
        Optional<AppSettings> settingsOpt = appSettingsRepository.findFirstByOrderByIdAsc();
        if (settingsOpt.isPresent()) {
            AppSettings settings = settingsOpt.get();
            Map<String, String> map = new HashMap<>();
            map.put("siteName", settings.getSiteName());
            map.put("siteDescription", settings.getSiteDescription());
            map.put("supportEmail", settings.getSupportEmail());
            map.put("currency", settings.getCurrency());
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

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getMobile(),
                user.getRole().name(),
                null,
                user.getCreatedAt()
        );
    }
}