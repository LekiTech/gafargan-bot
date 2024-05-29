package core.bothandler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import core.commands.ChatCommandProcessor;
import core.dictionary.parser.DictionaryRepository;
import core.dictionary.parser.JsonDictionary;
import core.database.DataStorage;
import org.springframework.context.ApplicationContext;

import static core.dictionary.parser.DictionaryParser.parse;

public class BotUpdates {

    private final TelegramBot bot;
    private final DictionaryRepository dictionaries = new JsonDictionary();
    private final ApplicationContext context;

    public BotUpdates(String apiToken, ApplicationContext context) {
        bot = new TelegramBot(apiToken);
        this.context = context;
    }

    public void start() throws Exception {
        dictionaries.setDictionaryByLang("lez", parse("lez_rus_dict"));
        dictionaries.setDictionaryByLang("rus", parse("rus_lez_dict"));
        bot.setUpdatesListener(updates -> {
            try {
                for (var update : updates) {
                    var textMessage = update.message();
                    var callbackQuery = update.callbackQuery();
                    if (textMessage != null) {
                        ChatCommandProcessor commandProcessor = CommandsFactory.createMessageProcessor(textMessage, dictionaries, bot, context);
                        commandProcessor.execute();
                        DataStorage.instance().saveSearch(textMessage.chat().id(), textMessage.text());
                    } else if (callbackQuery != null) {
                        var message = callbackQuery.message();
                        ChatCommandProcessor commandProcessor = CommandsFactory.createCallbackProcessor(message, dictionaries, bot, callbackQuery);
                        commandProcessor.execute();
                        DataStorage.instance().saveSearch(message.chat().id(), callbackQuery.data());
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