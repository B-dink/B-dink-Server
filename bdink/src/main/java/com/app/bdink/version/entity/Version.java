package com.app.bdink.version.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Version extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private Boolean forceUpdateRequired;

    private String releaseNotes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Builder
    public Version(
            String version,
            Boolean forceUpdateRequired,
            String releaseNotes,
            Platform platform) {
        this.version = version;
        this.forceUpdateRequired = forceUpdateRequired;
        this.releaseNotes = releaseNotes;
        this.platform = platform;
    }
}
