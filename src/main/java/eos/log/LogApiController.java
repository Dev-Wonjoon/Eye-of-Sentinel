package eos.log;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Log API", description = "로그 모니터링 데이터 제공 API")
@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class LogApiController {

    private final LogService logService;

    @Operation(summary = "최신 로그 조회", description = "OpenSearch에서 최근 발생한 로그 50건을 가져옵니다.")
    @GetMapping
    public List<LogDocument> getLogs() {
        return logService.getRecentLogs();
    }
}
