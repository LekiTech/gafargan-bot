package core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import core.parser.DictionaryParser;
import core.parser.DictionaryRepository;
import core.parser.JsonDictionary;
import core.parser.model.Example;

import java.util.List;

public class BotUpdates {

    private final TelegramBot bot;
    public static SelectDictionaryLanguage selectedLanguage;
    public static DictionaryRepository lezgiRusDictionary = new JsonDictionary();
    public static DictionaryRepository rusLezgiDictionary = new JsonDictionary();
    public static List<Example> listOfExample;

    public BotUpdates(String apiToken) {
        bot = new TelegramBot(apiToken);
    }

    public void start() throws Exception {
        selectedLanguage = new SelectDictionaryLanguage();
        DictionaryParser dictionaryParser = new DictionaryParser();
        lezgiRusDictionary.setDictionary(dictionaryParser.parse("lezgi_rus_dict_babakhanov_v2.json"));
        rusLezgiDictionary.setDictionary(dictionaryParser.parse("rus_lezgi_dict_hajiyev_v2.json"));
        listOfExample = Examples.getAll(List.of(lezgiRusDictionary, rusLezgiDictionary));
        bot.setUpdatesListener(updates -> {
            try {
                for (var update : updates) {
                    var textMessage = update.message();
                    var callbackQuery = update.callbackQuery();
                    if (textMessage != null) {
                        var commandProcessor = CommandsFactory.createMessageProcessor(textMessage, bot);
                        commandProcessor.execute();
                        DataStorage.instance().saveSearch(textMessage.chat().id(), textMessage.text());
                    } else if (callbackQuery != null) {
                        var commandProcessor = CommandsFactory.createCallbackProcessor(callbackQuery, bot);
                        commandProcessor.execute();
                        DataStorage.instance().saveSearch(callbackQuery.message().chat().id(), callbackQuery.data());
                    }
                }
            } catch (Exception e) {
                System.err.println(e);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}