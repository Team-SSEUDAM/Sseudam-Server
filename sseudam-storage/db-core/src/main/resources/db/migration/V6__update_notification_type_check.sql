-- type 컬럼의 데이터 타입을 VARCHAR(30)으로 변경
ALTER TABLE t_notification_stored ALTER COLUMN type TYPE VARCHAR(30);

-- NotificationType enum 값에 대한 체크 제약조건 추가
ALTER TABLE t_notification_stored DROP CONSTRAINT IF EXISTS t_notification_stored_type_check;
ALTER TABLE t_notification_stored ADD CONSTRAINT t_notification_stored_type_check 
CHECK (type IN (
    'APPROVE_SUGGESTION',
    'REJECT_SUGGESTION',
    'APPROVE_REPORT',
    'REJECT_REPORT',
    'NEW_PET_SEASON',
    'ANONYMOUS_VISITED_SPOT',
    'ADMIN_PUSH',
    'REGULAR'
));
