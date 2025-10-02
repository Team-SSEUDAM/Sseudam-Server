-- 기존 체크 제약조건 삭제 (존재하는 경우)
ALTER TABLE t_pet_point_history DROP CONSTRAINT IF EXISTS t_pet_point_history_point_action_check;

-- PetPointAction enum 값에 대한 체크 제약조건 추가
ALTER TABLE t_pet_point_history ADD CONSTRAINT t_pet_point_history_point_action_check 
CHECK (point_action IN (
    'SPOT_VISITED', 
    'TODAY_FIRST_SPOT_VISITED', 
    'REPORT', 
    'REPORT_APPROVED', 
    'SUGGESTION', 
    'SUGGESTION_APPROVED', 
    'ATTENDANCE', 
    'BONUS_ATTENDANCE', 
    'CONTINUITY_ATTENDANCE'
));

-- 다른 enum 필드들의 체크 제약조건도 확인 및 보완
-- (현재는 모두 올바르게 설정되어 있음)