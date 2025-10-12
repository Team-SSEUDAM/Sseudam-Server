-- t_spot_report 테이블에 reason 컬럼 추가
ALTER TABLE t_spot_report ADD COLUMN reason VARCHAR(255);

-- report_type 체크 제약조건 업데이트 (EMPTY_SPOT, ETC 추가)
ALTER TABLE t_spot_report DROP CONSTRAINT IF EXISTS t_spot_report_report_type_check;
ALTER TABLE t_spot_report ADD CONSTRAINT t_spot_report_report_type_check 
CHECK (report_type IN ('POINT', 'KIND', 'PHOTO', 'NAME', 'EMPTY_SPOT', 'ETC'));
