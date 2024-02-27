package core.bothandler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import core.commands.ChatCommandProcessor;
import core.config.DictionaryConfigReader;
import core.dictionary.parser.DictionaryRepository;
import core.dictionary.parser.ExamplesParsing;
import core.dictionary.parser.JsonDictionary;
import core.database.DataStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static core.dictionary.parser.DictionaryParser.parse;

/**
 * The BotUpdates class manages the updates received from the Telegram Bot API and handles them accordingly.
 * It initializes the TelegramBot instance and processes incoming updates by delegating to appropriate processors.
 */
public class BotUpdates {

    private final TelegramBot bot;
    private final DictionaryConfigReader dictionaryConfig = new DictionaryConfigReader();
    public static final DictionaryRepository lezgiRusDictionary = new JsonDictionary();
    public static final DictionaryRepository rusLezgiDictionary = new JsonDictionary();
    public static Map<String, Set<String>> listOfExample;

    public BotUpdates(String apiToken) {
        bot = new TelegramBot(apiToken);
    }

    /**
     * This method initializes and starts the functionality of a chat-bot. It sets up dictionaries for translation,
     * parses examples, and listens for updates from the bot's interface. Upon receiving updates, it processes text
     * messages and callback queries, executing corresponding commands and saving search data. If an exception occurs
     * during the execution, it prints the error to the standard error stream.
     *
     * @throws Exception
     */
    public void start() throws Exception {
        lezgiRusDictionary.setDictionary(parse(dictionaryConfig.getFilePath("lez_rus_dict")));
        rusLezgiDictionary.setDictionary(parse(dictionaryConfig.getFilePath("rus_lez_dict")));
        listOfExample = new ExamplesParsing().getAllExamples(List.of(lezgiRusDictionary, rusLezgiDictionary));
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