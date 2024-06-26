package core.dictionary.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.config.DictionaryPathConfig;
import core.dictionary.model.*;
import lombok.AllArgsConstructor;
import lombok.*;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static core.commands.CommandsList.*;

@Component
@AllArgsConstructor
public class DictionaryParser {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private final DictionaryPathConfig configReader;

    public Map<String, List<ExpressionDetails>> parse(String dictionaryKey) {
        Map<String, List<ExpressionDetails>> result = new HashMap<>();
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

    public Map<String, List<DialectDictionary.Dialect>> parseDialectDict(String dictionaryKey) {
        Map<String, List<DialectDictionary.Dialect>> result = new HashMap<>();
        String fileName = configReader.getFilePath(dictionaryKey);
        try {
            Resource resource = resourceLoader.getResource("classpath:" + fileName);
            DialectDictionary dictionary = objectMapper.readValue(resource.getInputStream(), DialectDictionary.class);
            List<DialectDictionary.Expression> expressions = dictionary.getExpressions();
            for (DialectDictionary.Expression expression : expressions) {
                String literaryDialect = expression.getSpelling();
                result.put(literaryDialect, expression.getDialects());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /* Temporary code due to the fact that the JSON format of the Lezgi-English dictionary is different from other dictionaries. */
    public Map<String, List<String>> parseLezEngDict(String dictionaryKey, ApplicationContext context) {
        Map<String, List<String>> result = new HashMap<>();
        /*
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
        System.out.println(result.size());
        System.out.println("**************************************************************");
        List<Expression> expressions = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            Expression expression = new Expression();
            String spelling = entry.getKey();
            expression.setSpelling(spelling.toUpperCase());

            List<ExpressionDetails> expressionDetailsList = new ArrayList<>();
            List<DefinitionDetails> definitionDetails = new ArrayList<>();
            List<Definition> definitionsList = new ArrayList<>();
            Definition definition = new Definition();
            for (String def : entry.getValue()) {
                if (definition.getValue() == null) {
                    definition.setValue(def);
                } else {
                    String value = definition.getValue() + " " + def;
                    definition.setValue(value);
                }
            }
            definitionsList.add(definition);
            definitionDetails.add(new DefinitionDetails(definitionsList, null, null));
            expressionDetailsList.add(new ExpressionDetails(null, definitionDetails, null));
            expression.setDetails(expressionDetailsList);
            expressions.add(expression);
        }
        System.out.println(expressions.size());
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(expressions);
            FileWriter writer = new FileWriter("lezgi_eng_dict_rasim_rasulov.json");
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("JSON file created successfully");

         */
        return result;
    }
/*
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
 */
}