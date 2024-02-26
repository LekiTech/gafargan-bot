package core.tgbothandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.dictionary.model.Expression;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class Test {

    public static void main(String[] args) throws JsonProcessingException {

        System.out.println(new Test().getExpressionBySpelling().getSpelling());
    }

    public Expression getExpressionBySpelling() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/v2/expressions/search?spelling={spelling}&expLang={expLang}&defLang={defLang}&similarCount={similarCount}";
        Map<String, String> params = new HashMap<>();
        params.put("spelling", "Хъач");
        params.put("expLang", "lez");
        params.put("defLang", "rus");
        params.put("similarCount", "0");
        String result = restTemplate.getForObject(url, String.class, params);

        ObjectMapper objectMapper = new ObjectMapper();
        record Similar(String id, String spelling) {}
        record Response(Expression found, List<Similar>similar) {}
        Response response = objectMapper.readValue(result, Response.class);

        return response.found;
    }

}
