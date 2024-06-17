package core.dictionary.parser;

import core.config.DictionaryPathConfig;
import core.dictionary.model.*;
import lombok.AllArgsConstructor;
import lombok.*;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class DictionaryParser {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public Map<String, List<ExpressionDetails>> parse(String dictionaryKey, ApplicationContext context) {
        Map<String, List<ExpressionDetails>> result = new HashMap<>();
        DictionaryPathConfig configReader = context.getBean(DictionaryPathConfig.class);
        String fileName = configReader.getFilePath(dictionaryKey);
        try {
            Resource resource = resourceLoader.getResource("classpath:" + fileName);
            Dictionary dictionary = objectMapper.readValue(resource.getInputStream(), Dictionary.class);
            List<Expression> expressions = dictionary.getExpressions();
            for (Expression expression : expressions) {
                result.put(expression.getSpelling().toLowerCase().replaceAll("ё", "е"),
                        expression.getDetails());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /* Temporary code due to the fact that the JSON format of the Lezgi-English dictionary is different from other dictionaries. */
    public Map<String, List<String>> parseLezEngDict(String dictionaryKey, ApplicationContext context) {
        Map<String, List<String>> result = new HashMap<>();
        DictionaryPathConfig configReader = context.getBean(DictionaryPathConfig.class);
        String fileName = configReader.getFilePath(dictionaryKey);
        try {
            Resource resource = resourceLoader.getResource("classpath:" + fileName);
            EngDictionary dictionary = objectMapper.readValue(resource.getInputStream(), EngDictionary.class);
            List<EngExpression> expressions = dictionary.getExpressions();
            for (EngExpression expression : expressions) {
                if (expression.getSpelling().endsWith("I")
                    || expression.getSpelling().endsWith("II")
                    || expression.getSpelling().endsWith("III")
                    || expression.getSpelling().endsWith("IV")) {
                    String spelling = expression.getSpelling().toLowerCase().split(" ")[0];
                    if (result.get(spelling) == null) {
                        result.put(spelling, expression.getDefinitions());
                    } else {
                        result.get(spelling).addAll(expression.getDefinitions());
                    }
                } else if (result.get(expression.getSpelling().toLowerCase()) == null) {
                    result.put(expression.getSpelling().toLowerCase(), expression.getDefinitions());
                } else {
                    result.get(expression.getSpelling().toLowerCase()).addAll(expression.getDefinitions());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Getter
    @Setter
    static class EngDictionary {
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
        private List<EngExpression> expressions;
    }

    @Getter
    @Setter
    static class EngExpression {
        private String spelling;
        private List<String> definitions;
    }
}