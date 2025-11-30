package eos.log;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class LogResponse {

    private Set<String> columns;
    private List<LogDocument> logs;
}
