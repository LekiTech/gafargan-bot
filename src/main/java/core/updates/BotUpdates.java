package core.updates;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import core.commands.ChatCommandProcessor;
import core.parser.DictionaryParser;
import core.parser.DictionaryRepository;
import core.parser.Examples;
import core.parser.JsonDictionary;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BotUpdates {

    private final TelegramBot bot;
    public static final DictionaryRepository lezgiRusDictionary = new JsonDictionary();
    public static final DictionaryRepository rusLezgiDictionary = new JsonDictionary();
    public static Map<String, Set<String>> listOfExample;

    public BotUpdates(String apiToken) {
        bot = new TelegramBot(apiToken);
    }

    public void start() throws Exception {
        DictionaryParser dictionaryParser = new DictionaryParser();
        lezgiRusDictionary.setDictionary(dictionaryParser.parse("lezgi_rus_dict_babakhanov_v2.json"));
        rusLezgiDictionary.setDictionary(dictionaryParser.parse("rus_lezgi_dict_hajiyev_v2.json"));
        Examples examples = new Examples();
        listOfExample = examples.getAll(List.of(lezgiRusDictionary, rusLezgiDictionary));
        bot.setUpdatesListener(updates -> {
            try {
                for (var update : updates) {
                    var textMessage = update.message();
                    var callbackQuery = update.callbackQuery();
                    if (textMessage != null) {
                        ChatCommandProcessor commandProcessor = CommandsFactory.createMessageProcessor(textMessage, bot);
                        commandProcessor.execute();
                        DataStorage.instance().saveSearch(textMessage.chat().id(), textMessage.text());
                    } else if (callbackQuery != null) {
                        ChatCommandProcessor commandProcessor = CommandsFactory.createCallbackProcessor(callbackQuery, bot);
                        commandProcessor.execute();
                        DataStorage.instance().saveSearch(callbackQuery.message().chat().id(), callbackQuery.data());
                    }
                }
            } catch (Exception e) {
                System.err.println(e);
            }
//            Runtime.getRuntime().gc();
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