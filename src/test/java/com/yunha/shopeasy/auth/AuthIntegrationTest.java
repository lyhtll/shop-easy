package com.yunha.shopeasy.auth;

import com.yunha.shopeasy.domain.auth.dto.request.LoginRequest;
import com.yunha.shopeasy.domain.auth.dto.request.SignUpRequest;
import com.yunha.shopeasy.domain.user.domain.User;
import com.yunha.shopeasy.domain.user.domain.UserRole;
import com.yunha.shopeasy.domain.user.repository.UserRepository;
import com.yunha.shopeasy.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("인증 통합 테스트")
class AuthIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공 — 201 반환")
    void signUp_success() throws Exception {
        SignUpRequest request = new SignUpRequest("test@example.com", "password1234", "홍길동");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 실패 — 이메일 중복 409")
    void signUp_duplicateEmail() throws Exception {
        userRepository.save(User.builder()
                .email("dup@example.com")
                .password(passwordEncoder.encode("password1234"))
                .name("기존회원")
                .role(UserRole.USER)
                .build());

        SignUpRequest request = new SignUpRequest("dup@example.com", "password1234", "신규회원");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("U002"));
    }

    @Test
    @DisplayName("회원가입 실패 — 잘못된 이메일 형식 400")
    void signUp_invalidEmail() throws Exception {
        SignUpRequest request = new SignUpRequest("not-an-email", "password1234", "홍길동");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패 — 비밀번호 8자 미만 400")
    void signUp_shortPassword() throws Exception {
        SignUpRequest request = new SignUpRequest("test@example.com", "1234567", "홍길동");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 성공 — accessToken, refreshToken 반환")
    void login_success() throws Exception {
        userRepository.save(User.builder()
                .email("login@example.com")
                .password(passwordEncoder.encode("password1234"))
                .name("로그인유저")
                .role(UserRole.USER)
                .build());

        LoginRequest request = new LoginRequest("login@example.com", "password1234");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("로그인 실패 — 비밀번호 오류 401")
    void login_wrongPassword() throws Exception {
        userRepository.save(User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password1234"))
                .name("유저")
                .role(UserRole.USER)
                .build());

        LoginRequest request = new LoginRequest("user@example.com", "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("U004"));
    }

    @Test
    @DisplayName("로그인 실패 — 존재하지 않는 이메일 401")
    void login_userNotFound() throws Exception {
        LoginRequest request = new LoginRequest("ghost@example.com", "password1234");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("U004"));
    }

    @Test
    @DisplayName("인증 없이 보호 엔드포인트 접근 — 401")
    void accessProtectedEndpoint_withoutToken() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }
}
