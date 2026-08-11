package com.furnihub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_settings")
public class AppSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "site_name", length = 100)
    private String siteName;

    @Column(name = "site_description", length = 255)
    private String siteDescription;

    @Column(name = "support_email", length = 100)
    private String supportEmail;

    @Column(name = "currency", length = 10)
    private String currency = "INR";

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @PreUpdate
    @PrePersist
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }
}
