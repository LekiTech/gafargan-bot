package core.dictionary.parser;

import core.config.DictionaryPathConfig;
import core.dictionary.model.*;
import lombok.AllArgsConstructor;
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
}