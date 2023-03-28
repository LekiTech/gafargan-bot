package core;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsedDictionary {

    public Map<String, List<String>> map = new HashMap<>();

    public void parse(String dictionaryPath) throws IOException {
        Gson gson = new Gson();

        // Читаем JSON из файла
        String json = readJsonFromFile(dictionaryPath);

        // Парсим его
        Dictionary dictionary = gson.fromJson(json, Dictionary.class);

        // Печатаем словарь
        List<Word> words = dictionary.getDictionary();

        // урус гафар
        for (Word word : words) {
            List<String> definitionsList = new ArrayList<>();
            //      map.put(word.getSpelling(), null);
            //лезги гафар
            for (String definition : word.getDefinitions()) {
                definitionsList.add(definition);
            }
            if (map.containsKey(word.getSpelling().toLowerCase())) {
//                List<String> list = map.get(word.getSpelling());
//                list.addAll(definitionsList);
//                map.put(word.getSpelling(), list);
                map.get(word.getSpelling().toLowerCase()).addAll(definitionsList);
            } else {
                map.put(word.getSpelling().toLowerCase(), definitionsList);
            }
        }
    }

    private static String readJsonFromFile(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
}