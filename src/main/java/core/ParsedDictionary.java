package core;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsedDictionary {

    public Map<String, List<String>> map = new HashMap<>();

    public void parse(String fileName) throws IOException {
        Gson gson = new Gson();
        /* Читаем JSON из файла */
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        String json = readJsonFromFile(is);
        /* Парсим его */
        Dictionary dictionary = gson.fromJson(json, Dictionary.class);
        /* Печатаем словарь */
        List<Word> words = dictionary.getDictionary();
        for (Word word : words) {
            List<String> definitionsList = new ArrayList<>();
            for (String definition : word.getDefinitions()) {
                definitionsList.add(definition);
            }
            if (map.containsKey(word.getSpelling().toLowerCase())) {
                map.get(word.getSpelling().toLowerCase()).addAll(definitionsList);
            } else {
                map.put(word.getSpelling().toLowerCase(), definitionsList);
            }
        }
    }

    private static String readJsonFromFile(InputStream is) throws IOException {
        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
}