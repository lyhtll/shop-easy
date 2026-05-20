package com.yunha.shopeasy.domain.user.controller;

import com.yunha.shopeasy.domain.user.dto.request.UpdateUserRequest;
import com.yunha.shopeasy.domain.user.dto.response.GetMeResponse;
import com.yunha.shopeasy.domain.user.usecase.GetMeUseCase;
import com.yunha.shopeasy.domain.user.usecase.UpdateMeUseCase;
import com.yunha.shopeasy.global.security.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final GetMeUseCase getMeUseCase;
    private final UpdateMeUseCase updateMeUseCase;

    @GetMapping("/me")
    public ResponseEntity<GetMeResponse> getMe() {
        return ResponseEntity.ok(getMeUseCase.execute(SecurityUtil.getCurrentUserId()));
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateMe(@Valid @RequestBody UpdateUserRequest request) {
        updateMeUseCase.execute(SecurityUtil.getCurrentUserId(), request);
        return ResponseEntity.noContent().build();
    }
}