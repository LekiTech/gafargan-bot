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
public class DefinitionDetails {

    @JsonProperty("definitions")
    private List<Definition> definitions;

    @JsonProperty("examples")
    private List<Example> examples;

    @JsonProperty("tags")
    private List<String> tags;
}