-- ================================
-- 인덱스 생성 스크립트
-- ================================

-- ================================
-- 사용자 관련 인덱스
-- ================================

-- 사용자 테이블 인덱스
CREATE INDEX idx_users_user_key ON t_users(user_key);
CREATE INDEX idx_users_email_deleted ON t_users(email, deleted_at);
CREATE INDEX idx_users_nickname_deleted ON t_users(nickname, deleted_at);
CREATE INDEX idx_users_deleted_at ON t_users(deleted_at);

-- 사용자 디바이스 테이블 인덱스
CREATE INDEX idx_user_device_user_id_deleted ON t_user_device(user_id, deleted_at);
CREATE INDEX idx_user_device_user_key ON t_user_device(user_key);
CREATE INDEX idx_user_device_deleted_at ON t_user_device(deleted_at);

-- ================================
-- 인증 관련 인덱스
-- ================================

-- 인증 히스토리 테이블 인덱스
CREATE INDEX idx_auth_history_user_key_device ON t_authentication_history(user_key, device_id);
CREATE INDEX idx_auth_history_user_key_status ON t_authentication_history(user_key, entity_status);
CREATE INDEX idx_auth_history_user_id_status ON t_authentication_history(user_id, entity_status);
CREATE INDEX idx_auth_history_access_token ON t_authentication_history(access_token);

-- ================================
-- 쓰레기통 관련 인덱스
-- ================================

-- 쓰레기통 테이블 인덱스
CREATE INDEX idx_t_trash_spot_point_gist ON t_trash_spot USING GIST(point);
CREATE INDEX idx_t_trash_spot_gist_region_point ON t_trash_spot USING GIST(region, point);
CREATE INDEX idx_t_trash_spot_region_active ON t_trash_spot USING BTREE(region) WHERE deleted_at IS NULL;
CREATE INDEX idx_t_trash_spot_trash_type_active ON t_trash_spot USING BTREE(trash_type) WHERE deleted_at IS NULL;

-- 쓰레기통 이미지 테이블 인덱스
CREATE INDEX idx_trash_spot_image_spot_id ON t_trash_spot_image(trash_spot_id);
CREATE INDEX idx_trash_spot_image_deleted_at ON t_trash_spot_image(deleted_at);

-- ================================
-- 신고/제안 관련 인덱스
-- ================================

-- 쓰레기통 신고 테이블 인덱스
CREATE INDEX idx_spot_report_user_id ON t_spot_report(user_id);
CREATE INDEX idx_spot_report_spot_name ON t_spot_report(spot_name);
CREATE INDEX idx_spot_report_report_type ON t_spot_report(report_type);
CREATE INDEX idx_spot_report_created_at ON t_spot_report(created_at DESC);
CREATE INDEX idx_spot_report_deleted_at ON t_spot_report(deleted_at);

-- 신고 거절 테이블 인덱스
CREATE INDEX idx_reject_report_report_id ON t_reject_report(report_id);

-- 쓰레기통 제안 테이블 인덱스
CREATE INDEX idx_spot_suggestion_user_id ON t_spot_suggestion(user_id);
CREATE INDEX idx_spot_suggestion_spot_name ON t_spot_suggestion(spot_name);
CREATE INDEX idx_spot_suggestion_site_deleted ON t_spot_suggestion(site, deleted_at);
CREATE INDEX idx_spot_suggestion_status ON t_spot_suggestion(status);
CREATE INDEX idx_spot_suggestion_created_at ON t_spot_suggestion(created_at DESC);
CREATE INDEX idx_spot_suggestion_deleted_at ON t_spot_suggestion(deleted_at);
-- 공간 인덱스
CREATE INDEX idx_spot_suggestion_point ON t_spot_suggestion USING GIST(point);
CREATE INDEX idx_spot_suggestion_active_point ON t_spot_suggestion USING GIST(point) WHERE deleted_at IS NULL;

-- ================================
-- 방문/출석 관련 인덱스
-- ================================

-- 쓰레기통 방문 테이블 인덱스
CREATE INDEX idx_spot_visited_user_id ON t_spot_visited(user_id);
CREATE INDEX idx_spot_visited_spot_id ON t_spot_visited(spot_id);
CREATE INDEX idx_spot_visited_user_spot_created ON t_spot_visited(user_id, spot_id, created_at DESC);
CREATE INDEX idx_spot_visited_user_date ON t_spot_visited(user_id, date);
CREATE INDEX idx_spot_visited_user_spot_date ON t_spot_visited(user_id, spot_id, date);

-- 출석 테이블 인덱스
CREATE INDEX idx_attendance_user_id_date ON t_attendance(user_id, date DESC);

-- ================================
-- 펫 관련 인덱스
-- ================================

-- 펫 테이블 인덱스
CREATE INDEX idx_pet_year_monthly ON t_pet(year, monthly);

-- 사용자 펫 테이블 인덱스
CREATE INDEX idx_user_pet_user_id_deleted ON t_user_pet(user_id, deleted_at);
CREATE INDEX idx_user_pet_deleted_at ON t_user_pet(deleted_at);
CREATE INDEX idx_user_pet_user_ids_deleted ON t_user_pet(user_id) WHERE deleted_at IS NULL;

-- 펫 포인트 히스토리 테이블 인덱스
CREATE INDEX idx_pet_point_history_user_pet_id ON t_pet_point_history(user_pet_id);

-- 펫 레벨업 히스토리 테이블 인덱스
CREATE INDEX idx_pet_level_up_history_year_monthly_user_pet ON t_pet_level_up_history(year, monthly, user_pet_id);
CREATE INDEX idx_pet_level_up_history_user_pet_id ON t_pet_level_up_history(user_pet_id);
CREATE INDEX idx_pet_level_up_history_user_id ON t_pet_level_up_history(user_id);

-- ================================
-- 알림 관련 인덱스
-- ================================

-- 저장된 알림 테이블 인덱스
CREATE INDEX idx_notification_stored_user_id_read_status ON t_notification_stored(user_id, read_status);

-- ================================
-- 버전 관리 인덱스
-- ================================

-- 앱 버전 테이블 인덱스
CREATE INDEX idx_app_version_device_type ON t_app_version(device_type);