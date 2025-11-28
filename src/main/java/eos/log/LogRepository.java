package eos.log;

import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.SortOrder;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class LogRepository {
    private final OpenSearchClient client;

    private static final String INDEX_PATTERN = "eos-logs*";

    /*
    * 지정된 개수만큼 최신 로그를 조회합니다. (like "tail n")
    *
    * @param size 조회할 로그의 최대 개수
    * @return 최신순으로 정렬된 로그 리스트
    * @throws IOException OpenSearch 통신 중 네트워크 에러 발생 시
    */
    public List<LogDocument> findRecentLogs(int size) throws IOException {
        SearchResponse<LogDocument> response = client.search(s -> s
                .index(INDEX_PATTERN)
                .query(q -> q.matchAll(m -> m))
                .sort(sort -> sort.field(f -> f
                        .field("@timestamp")
                        .order(SortOrder.Desc)))
                .size(size),
                LogDocument.class
        );
        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
}
