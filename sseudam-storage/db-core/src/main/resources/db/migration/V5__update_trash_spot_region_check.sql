-- UNKNOWN 지역을 t_trash_spot 테이블에도 추가
ALTER TABLE t_trash_spot DROP CONSTRAINT IF EXISTS t_trash_spot_region_check;
ALTER TABLE t_trash_spot ADD CONSTRAINT t_trash_spot_region_check
CHECK (region IN (
    'SEOUL', 'GYEONGGI', 'BUSAN', 'DAEGU', 'INCHEON', 'GWANGJU',
    'DAEJEON', 'ULSAN', 'SEJONG', 'GANGWON', 'CHUNGBUK', 'CHUNGNAM',
    'JEONBUK', 'JEONNAM', 'GYEONGBUK', 'GYEONGNAM', 'JEJU', 'UNKNOWN'
));
