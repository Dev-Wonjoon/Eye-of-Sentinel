package eos.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    private static final int DEFAULT_FETCH_SIZE = 50;

    /*
     * 대시보드 표출을 위한 동적 헤더(컬럼)와 로그 데이터를 함께 조회합니다.
     * OpenSearch 장애 시 예외를 캐치하여 빈 데이터를 반환하는 Fail-Safe 처리가 적용되어 있습니다.
     * @ param indexPattern 사용자가 입력한 인덱스 패턴
     * @return 컬럼 목록("columns")과 로그 리스트("logs")가 담긴 Map 객체
     */
    public LogResponse getDynamicDashboardData(String indexPattern) {
        String targetIndex = (indexPattern == null || indexPattern.isBlank()) ? "eos-logs*" : indexPattern;

        try {
            Set<String> columns = logRepository.findIndexFields(targetIndex);
            List<LogDocument> logs = logRepository.findRecentLogs(targetIndex, DEFAULT_FETCH_SIZE);
            return new LogResponse(columns, logs);
        } catch(Exception e) {
            log.error("Failed to fetch logs", e);
            return new LogResponse(Collections.emptySet(), Collections.emptyList());
        }
    }

    public List<String> getIndexList() {
        try {
            return logRepository.findIndexList();
        } catch (Exception e) {
            log.error("Failed to fetch index list from cluster");
            return Collections.emptyList();
        }
    }
}
