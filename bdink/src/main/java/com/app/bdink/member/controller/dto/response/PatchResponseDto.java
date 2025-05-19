package com.app.bdink.member.controller.dto.response;

public record PatchResponseDto(
        Long patchedResourceId,
        OperationType operationType
) {
    public static PatchResponseDto from(Long patchedResourceId, OperationType operationType) {
        return new PatchResponseDto(patchedResourceId, operationType);
    }
}