package eos.log;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDocument {
    private String id;

    @JsonProperty("@timestamp")
    private String timestamp;
    private String level;
    private String service;
    private String message;

    private Map<String, Object> details = new HashMap<>();

    @JsonAnySetter
    public void addDetail(String key, Object value) {
        this.details.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getDetails() {
        return details;
    }

    public Object get(String key) {
        if ("@timestamp".equals(key)) return timestamp;
        if ("level".equals(key)) return level;
        if ("service".equals(key)) return service;
        if ("message".equals(key)) return message;
        if ("_id".equals(key)) return id;

        return details.get(key);
    }
}