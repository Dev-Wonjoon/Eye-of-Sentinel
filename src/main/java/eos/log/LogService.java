package eos.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    private static final int DEFAULT_FETCH_SIZE = 50;

    /*
     * 대시보드용 최신 로그를 조회합니다.
     * OpenSearch 장애 발생 시 전체 시스템 장애로 번지는 것을 막기 위해
     * 예외를 잡아서 빈 리스트를 반환하는 'Fail-Safe' 전략을 사용합니다.
     *
     * @return 로그 리스트 (조회 실패 시 빈 리스트 반환)
     */
    public List<LogDocument> getRecentLogs() {
        try {
            return logRepository.findRecentLogs(DEFAULT_FETCH_SIZE);
        } catch (Exception e) {
            log.error("Failed to fetch logs from OpenSearch. Index: eos-logs*", e);

            return Collections.emptyList();
        }
    }
}
