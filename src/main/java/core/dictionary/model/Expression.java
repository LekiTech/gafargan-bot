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
public class Expression {

    @JsonProperty("spelling")
    private String spelling;

    @JsonProperty("details")
    private List<ExpressionDetails> details;
}