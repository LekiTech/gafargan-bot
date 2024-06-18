package core.dictionary.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class DialectDictionary {

    private String name;
    private String authors;
    private String publicationYear;
    private String providedBy;
    private String providedByURL;
    private String processedBy;
    private String seeSourceURL;
    private String expressionLanguageId;
    private String definitionLanguageId;
    private List<Expression> expressions;

    @Getter
    @Setter
    public static class Expression {
        private String spelling;
        private List<Dialect> dialects;
    }

    @Getter
    @Setter
    public static class Dialect {
        private String name;
        private String spelling;
    }
}