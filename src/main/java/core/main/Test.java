package core.main;

import org.springframework.web.client.RestTemplate;

import java.util.*;

public class Test {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/v2/expressions/search?spelling={spelling}&expLang={expLang}&defLang={defLang}&similarCount={similarCount}";
        Map<String, String> params = new HashMap<>();
        params.put("spelling", "пуш");
        params.put("expLang", "lez");
        params.put("defLang", "rus");
        params.put("similarCount", "0");
        String result = restTemplate.getForObject(url, String.class, params);
        System.out.println(result);
    }

}
