package eos.log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class LogRepository {
    private final RestClient client;
    private final ObjectMapper objectMapper;
    private JsonNode rootNode;

    /*
     * 인덱스 필드 목록 조회
     */
    public Set<String> findIndexFields(String indexPattern) throws IOException {
        Request request = new Request("GET", "/" + indexPattern + "/_mapping");
        Response response = client.performRequest(request);

        Map<String, Object> rootMap = objectMapper.readValue(response.getEntity().getContent(), Map.class);

        Set<String> fields = new LinkedHashSet<>();

        fields.add("@timestamp");
        fields.add("level");
        fields.add("service");
        fields.add("message");

        for (Object indexValue : rootMap.values()) {
            Map<String, Object> indexMap = (Map<String, Object>) indexValue;
            Map<String, Object> mappings = (Map<String, Object>) indexMap.get("mappings");
            if (mappings == null) continue;

            Map<String, Object> properties = (Map<String, Object>) mappings.get("properties");
            if (properties != null) {
                fields.addAll(properties.keySet());
            }
        }
        return fields;
    }

    /*
     * 로그 데이터 검색
     */
    public List<LogDocument> findRecentLogs(String indexPattern, int size) throws IOException {
        Request request = new Request("POST", "/" + indexPattern + "/_search");
        String jsonQuery = String.format("""
            {
                "size": %d,
                "query": { "match_all": {} },
                "sort": [ { "@timestamp": "desc" } ]
            }""", size);

        request.setJsonEntity(jsonQuery);
        Response response = client.performRequest(request);

        JsonNode rootNode = objectMapper.readTree(response.getEntity().getContent());
        JsonNode hitsNode = rootNode.path("hits").path("hits");

        List<LogDocument> logs = new ArrayList<>();
        if (hitsNode.isArray()) {
            for (JsonNode hit : hitsNode) {
                LogDocument doc = objectMapper.treeToValue(hit.path("_source"), LogDocument.class);

                if (doc != null) {
                    doc.setId(hit.path("_id").asText());
                    logs.add(doc);
                }
            }
        }
        return logs;
    }

    /*
     * 인덱스 목록 조회
     */
    public List<String> findIndexList() throws IOException {
        Request request = new Request("GET", "/_cat/indices?format=json&h=index");
        Response response = client.performRequest(request);

        String responseBody = EntityUtils.toString(response.getEntity());

        List<Map<String, String>> indicesMap = objectMapper.readValue(
                responseBody,
                new TypeReference<List<Map<String, String>>>() {}
        );

        List<String> indices = new ArrayList<>();
        for (Map<String, String> indexMap : indicesMap) {
            Object indexNameObj = indexMap.get("index");
            if (indexNameObj != null) {
                String indexName = indexNameObj.toString();
                if (!indexName.startsWith(".")) {
                    indices.add(indexName);
                }
            }
        }

        Collections.sort(indices);
        return indices;
    }
}
