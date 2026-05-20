package com.yunha.shopeasy.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank String name
) {}