package eos.log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
* OpenSearch 로그 문서를 담는 DTO
* 실제 인덱스에 저장된 JSON 구조와 매핑됩니다.
* 불필요한 필드로 인한 매핑 에러를 방지하기 위해 ignoreUnknown 설정을 적용했습니다.
* */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogDocument {
    private String id;
    @JsonProperty("@timestamp")
    private String timestamp;
    private String level;
    private String message;
    private String service; // 로그를 생성한 서비스
}
