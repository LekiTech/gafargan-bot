package core.parser.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Example {
    private String raw;
    private String src;
    private String trl;
    private List<String> tags;
}