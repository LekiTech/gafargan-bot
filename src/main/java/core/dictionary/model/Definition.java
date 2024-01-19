package core.dictionary.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Definition {
    private String value;
    private List<String> tags;
}