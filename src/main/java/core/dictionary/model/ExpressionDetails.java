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
public class ExpressionDetails {

//    private String gr;

    @JsonProperty("inflection")
    private String inflection;

    @JsonProperty("definitionDetails")
    private List<DefinitionDetails> definitionDetails;

    @JsonProperty("examples")
    private List<Example> examples;
}