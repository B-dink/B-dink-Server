package com.app.bdink.oauth2.domain;

import jakarta.validation.constraints.NotBlank;

public record PasswordDto (
        @NotBlank
        String origin,
        @NotBlank
        String copy
){

}
