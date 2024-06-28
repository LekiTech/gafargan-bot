package core.bothandler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import core.commands.ChatCommandProcessor;
import core.config.Env;
import core.database.entity.Search;
import core.database.entity.UserChatId;
import core.database.service.SearchService;
import core.database.service.UserChatIdService;
import core.dictionary.parser.DictionaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static core.config.Mailing.startMailing;

@Component
@AllArgsConstructor
public class BotUpdates {

    private final DictionaryRepository dictionaryRepository;
    private final ApplicationContext context;
    private final UserChatIdService userChatIdService;
    private final SearchService searchService;

    public void start() throws Exception {
        final TelegramBot bot = context.getBean(TelegramBot.class);
        final ExecutorService executor = Executors.newFixedThreadPool(10);
        bot.setUpdatesListener(updates -> {
            try {
                for (var update : updates) {
                    executor.submit(() -> {
                        try {
                            var textMessage = update.message();
                            var callbackQuery = update.callbackQuery();
                            if (textMessage != null
                                && textMessage.chat().id().equals(Env.instance().getSecretId())
                                && textMessage.text().contains(Env.instance().getSecretKey())) {
                                startMailing(textMessage.text(), context, bot);
                            } else if (textMessage != null) {
                                saveSearchInDataBase(textMessage.text(), textMessage.chat().id());
                                ChatCommandProcessor commandProcessor =
                                        CommandsFactory.createMessageProcessor(textMessage, dictionaryRepository, bot, context);
                                commandProcessor.execute();
                            } else if (callbackQuery != null) {
                                var message = callbackQuery.message();
                                saveSearchInDataBase(callbackQuery.data(), message.chat().id());
                                ChatCommandProcessor commandProcessor =
                                        CommandsFactory.createCallbackProcessor(message, dictionaryRepository, bot, callbackQuery);
                                assert commandProcessor != null;
                                commandProcessor.execute();
                                bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
                            }
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    });
                }
            } catch (Exception e) {
                System.err.println(e);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void saveSearchInDataBase(String value, Long chatId) {
        userChatIdService.saveUser(new UserChatId(chatId, new Timestamp(System.currentTimeMillis())));
        searchService.saveSearch(new Search(
                UUID.randomUUID(),
                value,
                new UserChatId(chatId, new Timestamp(System.currentTimeMillis())),
                new Timestamp(System.currentTimeMillis()))
        );
    }
}