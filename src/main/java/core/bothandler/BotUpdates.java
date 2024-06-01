package core.bothandler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import core.commands.ChatCommandProcessor;
import core.database.entity.Search;
import core.database.entity.UserChatId;
import core.database.service.SearchService;
import core.database.service.UserChatIdService;
import core.dictionary.parser.DictionaryRepository;
import core.dictionary.parser.JsonDictionary;
import core.dictionary.parser.DictionaryParser;
import org.springframework.context.ApplicationContext;

import java.sql.Timestamp;
import java.util.UUID;

import static core.commands.CommandsList.*;

public class BotUpdates {

    private final TelegramBot bot;
    private final DictionaryRepository dictionaryRepository = new JsonDictionary();
    private final ApplicationContext context;

    public BotUpdates(String apiToken, ApplicationContext context) {
        bot = new TelegramBot(apiToken);
        this.context = context;
    }

    public void start() throws Exception {
        DictionaryParser reader = context.getBean(DictionaryParser.class);
        dictionaryRepository.setDictionary(LEZ, reader.parse(LEZ, context));
        dictionaryRepository.setDictionary(RUS, reader.parse(RUS, context));
        bot.setUpdatesListener(updates -> {
            try {
                for (var update : updates) {
                    var textMessage = update.message();
                    var callbackQuery = update.callbackQuery();
                    if (textMessage != null) {
                        saveSearchInDataBase(textMessage.text(), textMessage.chat().id());
                        ChatCommandProcessor commandProcessor = CommandsFactory
                                .createMessageProcessor(textMessage, dictionaryRepository, bot, context);
                        commandProcessor.execute();
                    } else if (callbackQuery != null) {
                        var message = callbackQuery.message();
                        saveSearchInDataBase(callbackQuery.data(), message.chat().id());
                        ChatCommandProcessor commandProcessor = CommandsFactory
                                .createCallbackProcessor(message, dictionaryRepository, bot, callbackQuery, context);
                        commandProcessor.execute();
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

    private void saveSearchInDataBase(String value, Long chatId) {
        UserChatIdService userChatIdService = context.getBean(UserChatIdService.class);
        userChatIdService.saveUser(new UserChatId(chatId, new Timestamp(System.currentTimeMillis())));
        SearchService searchService = context.getBean(SearchService.class);
        searchService.saveSearch(new Search(
                UUID.randomUUID(),
                value,
                new UserChatId(chatId, new Timestamp(System.currentTimeMillis())),
                new Timestamp(System.currentTimeMillis()))
        );
    }
}