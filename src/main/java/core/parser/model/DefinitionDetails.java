package core.parser.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefinitionDetails {
    private List<Definition> definitions;
    private List<Example> examples;
    private List<String> tags;
}