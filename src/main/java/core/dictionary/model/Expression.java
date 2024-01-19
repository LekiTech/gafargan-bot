package core.dictionary.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expression {
    private String spelling;
    private List<ExpressionDetails> details;
}