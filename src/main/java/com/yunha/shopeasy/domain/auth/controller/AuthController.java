package com.yunha.shopeasy.domain.auth.controller;

import com.yunha.shopeasy.domain.auth.dto.request.LoginRequest;
import com.yunha.shopeasy.domain.auth.dto.request.ReissueRequest;
import com.yunha.shopeasy.domain.auth.dto.request.SignUpRequest;
import com.yunha.shopeasy.domain.auth.dto.response.LoginResponse;
import com.yunha.shopeasy.domain.auth.usecase.LoginUseCase;
import com.yunha.shopeasy.domain.auth.usecase.LogoutUseCase;
import com.yunha.shopeasy.domain.auth.usecase.ReissueUseCase;
import com.yunha.shopeasy.domain.auth.usecase.SignUpUseCase;
import com.yunha.shopeasy.global.security.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignUpUseCase signUpUseCase;
    private final LoginUseCase loginUseCase;
    private final ReissueUseCase reissueUseCase;
    private final LogoutUseCase logoutUseCase;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        signUpUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginUseCase.execute(request));
    }

    @PostMapping("/reissue")
    public ResponseEntity<LoginResponse> reissue(@Valid @RequestBody ReissueRequest request) {
        return ResponseEntity.ok(reissueUseCase.execute(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        logoutUseCase.execute(SecurityUtil.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }
}