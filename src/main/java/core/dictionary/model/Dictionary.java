package core.dictionary.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dictionary {
    private String name;
    private String authors;
    private String publicationYear;
    private String description;
    private String providedBy;
    private String providedByURL;
    private String processedBy;
    private String copyright;
    private String seeSourceURL;
    private String expressionLanguageId;
    private String definitionLanguageId;
    private List<Expression> expressions;
}