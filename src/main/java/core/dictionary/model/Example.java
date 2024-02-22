package core.dictionary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Example {

    @JsonProperty("raw")
    private String raw;

    @JsonProperty("src")
    private String src;

    @JsonProperty("trl")
    private String trl;

    @JsonProperty("tags")
    private List<String> tags;
}