package com.yunha.shopeasy.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yunha.shopeasy.domain.auth.repository.RefreshTokenRepository;
import com.yunha.shopeasy.domain.user.domain.UserRole;
import com.yunha.shopeasy.global.security.jwt.provider.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@Transactional
public abstract class IntegrationTestSupport {

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    protected final ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules();

    @Autowired
    protected JwtProvider jwtProvider;

    @MockitoBean
    protected RedisTemplate<String, String> redisTemplate;

    @MockitoBean
    protected RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setUpMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUpMocks() {
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        lenient().when(valueOps.get(anyString())).thenReturn(null);
        lenient().when(refreshTokenRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    protected String userToken(Long userId) {
        return "Bearer " + jwtProvider.generateAccessToken(userId, UserRole.USER);
    }

    protected String adminToken(Long userId) {
        return "Bearer " + jwtProvider.generateAccessToken(userId, UserRole.ADMIN);
    }
}
