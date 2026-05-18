-- 로그인 시 이메일 조회 (매 로그인마다 발생)
CREATE INDEX idx_users_email ON users (email);

-- 내 주문 목록 조회 (user_id 기준 필터)
CREATE INDEX idx_orders_user_id ON orders (user_id);

-- 주문 상태별 필터링 및 상태 전이 검증
CREATE INDEX idx_orders_status ON orders (status);

-- 주문-결제 조인 및 결제 정보 조회
CREATE INDEX idx_payments_order_id ON payments (order_id);