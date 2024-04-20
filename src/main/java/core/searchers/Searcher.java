package core.searchers;

import core.dictionary.parser.DictionaryRepository;

public interface Searcher {

    Response searchResponse(String lang, DictionaryRepository dictionaries, String userMessage);
}