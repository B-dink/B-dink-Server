package com.app.bdink.message.entity;

import com.app.bdink.message.controller.dto.TokenData;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class AlimtalkToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    public AlimtalkToken(TokenData dto) {
        this.token = dto.accessToken();
    }
}