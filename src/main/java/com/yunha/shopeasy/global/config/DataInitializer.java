package com.yunha.shopeasy.global.config;

import com.yunha.shopeasy.domain.product.domain.Product;
import com.yunha.shopeasy.domain.product.domain.ProductCategory;
import com.yunha.shopeasy.domain.product.repository.ProductRepository;
import com.yunha.shopeasy.domain.user.domain.User;
import com.yunha.shopeasy.domain.user.domain.UserRole;
import com.yunha.shopeasy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        userRepository.saveAll(List.of(
                User.builder()
                        .email("admin@shop.com")
                        .password(passwordEncoder.encode("Admin1234!"))
                        .name("관리자")
                        .role(UserRole.ADMIN)
                        .build(),
                User.builder()
                        .email("user@shop.com")
                        .password(passwordEncoder.encode("User1234!"))
                        .name("홍길동")
                        .role(UserRole.USER)
                        .build(),
                User.builder()
                        .email("user2@shop.com")
                        .password(passwordEncoder.encode("User1234!"))
                        .name("김철수")
                        .role(UserRole.USER)
                        .build()
        ));

        productRepository.saveAll(List.of(
                Product.builder()
                        .name("갤럭시 S25 Ultra")
                        .price(new BigDecimal("1599000"))
                        .stock(50)
                        .category(ProductCategory.ELECTRONICS)
                        .description("Samsung 최신 플래그십 스마트폰")
                        .build(),
                Product.builder()
                        .name("애플 아이폰 16 Pro")
                        .price(new BigDecimal("1550000"))
                        .stock(30)
                        .category(ProductCategory.ELECTRONICS)
                        .description("Apple 최신 프리미엄 스마트폰")
                        .build(),
                Product.builder()
                        .name("나이키 에어맥스 270")
                        .price(new BigDecimal("149000"))
                        .stock(100)
                        .category(ProductCategory.SPORTS)
                        .description("편안한 쿠셔닝의 러닝화")
                        .build(),
                Product.builder()
                        .name("클린 코드 (로버트 C. 마틴)")
                        .price(new BigDecimal("33000"))
                        .stock(200)
                        .category(ProductCategory.BOOKS)
                        .description("소프트웨어 장인 정신을 위한 필독서")
                        .build(),
                Product.builder()
                        .name("린넨 오버사이즈 셔츠")
                        .price(new BigDecimal("45000"))
                        .stock(80)
                        .category(ProductCategory.CLOTHING)
                        .description("시원하고 편안한 여름 린넨 셔츠")
                        .build(),
                Product.builder()
                        .name("제주 감귤 2kg")
                        .price(new BigDecimal("18000"))
                        .stock(150)
                        .category(ProductCategory.FOOD)
                        .description("제주도 직송 신선한 감귤")
                        .build(),
                Product.builder()
                        .name("로지텍 MX Master 3")
                        .price(new BigDecimal("119000"))
                        .stock(60)
                        .category(ProductCategory.ELECTRONICS)
                        .description("생산성을 높이는 프리미엄 무선 마우스")
                        .build(),
                Product.builder()
                        .name("요가 매트 6mm")
                        .price(new BigDecimal("29000"))
                        .stock(120)
                        .category(ProductCategory.SPORTS)
                        .description("미끄럼 방지 TPE 소재 요가 매트")
                        .build(),
                Product.builder()
                        .name("에센셜 오일 라벤더 30ml")
                        .price(new BigDecimal("15000"))
                        .stock(90)
                        .category(ProductCategory.BEAUTY)
                        .description("천연 아로마 테라피용 라벤더 에센셜 오일")
                        .build(),
                Product.builder()
                        .name("원목 책상 1200x600")
                        .price(new BigDecimal("250000"))
                        .stock(20)
                        .category(ProductCategory.HOME)
                        .description("천연 원목으로 제작된 홈 오피스 책상")
                        .build()
        ));
    }
}