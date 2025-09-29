-- 제보 거절 테이블
CREATE TABLE t_reject_suggestion (
    id BIGSERIAL PRIMARY KEY,
    suggestion_id BIGINT NOT NULL,
    reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);
