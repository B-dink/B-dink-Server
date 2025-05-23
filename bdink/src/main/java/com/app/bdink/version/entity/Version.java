package com.app.bdink.version.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String currentVersion;

    @Column(nullable = false)
    private String minimumRequiredVersion;

    private Boolean forceUpdateRequired;

    private String releaseNotes;

    private LocalDateTime releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Builder
    public Version(
            String currentVersion,
            String minimumRequiredVersion,
            Boolean forceUpdateRequired,
            String releaseNotes,
            LocalDateTime releaseDate,
            Platform platform) {
        this.currentVersion = currentVersion;
        this.minimumRequiredVersion = minimumRequiredVersion;
        this.forceUpdateRequired = forceUpdateRequired;
        this.releaseNotes = releaseNotes;
        this.releaseDate = releaseDate;
        this.platform = platform;
    }
}
