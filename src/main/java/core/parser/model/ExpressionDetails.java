package core.parser.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpressionDetails {
    private String gr;
    private String inflection;
    private List<DefinitionDetails> definitionDetails;
    private List<Example> examples;
}