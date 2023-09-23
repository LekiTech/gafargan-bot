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
        var examples = new Examples();
        listOfExample = examples.getAll(List.of(lezgiRusDictionary, rusLezgiDictionary));
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
            Runtime.getRuntime().gc();
//            MemoryMXBean memBean = ManagementFactory.getMemoryMXBean() ;
//            MemoryUsage heapMemoryUsage = memBean.getHeapMemoryUsage();
//            System.out.println("Committed memory: " + heapMemoryUsage.getCommitted()); // given memory to JVM by OS ( may fail to reach getMax, if there isn't more memory)
//            System.out.println("Used memory: " + heapMemoryUsage.getUsed()); // used now by your heap
//            System.out.println("Max memory: " + heapMemoryUsage.getMax()); // max memory allowed for jvm -Xmx flag (-1 if isn't specified)
//            System.out.println("Free memory: " + Runtime.getRuntime().freeMemory());
//            System.out.println("=================================================================");
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}