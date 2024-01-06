package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import core.DataStorage;
import core.parser.DictionaryRepository;
import core.searchers.Answer;
import core.searchers.SearchInDictionary;

import java.util.*;

import static core.BotUpdates.*;
import static core.CommandsFactory.COMMAND_EXAMPLE_SUFFIX;

public class WordSearchCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;

    public WordSearchCommandProcessor(Message message, TelegramBot bot) {
        this.message = message;
        this.bot = bot;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        var userMessage = message.text().toLowerCase().replaceAll("[i1lӏ|!]", "I").replaceAll("ё", "е");
        var language = DataStorage.instance().getLastSelectedDictionary(chatId);
        switch (language) {
            case CommandsList.LEZGI_RUS -> sendAnswerToUser(lezgiRusDictionary, userMessage, chatId);
            case CommandsList.RUS_LEZGI -> sendAnswerToUser(rusLezgiDictionary, userMessage, chatId);
            default -> {
            }
        }
    }

    public void sendAnswerToUser(DictionaryRepository dictionary, String userMessage, Long chatId) {
        SearchInDictionary search = new SearchInDictionary();
        Answer answer = search.sendAnswerFromDictionary(dictionary, userMessage);
        if (answer.exampleButton() == null || answer.exampleButton().isEmpty()) {
            bot.execute(new SendMessage(chatId, answer.messageText())
                    .parseMode(ParseMode.HTML));
        } else {
            InlineKeyboardMarkup inlineKeyboard = createInlineKeyboard(answer.exampleButton());
            bot.execute(new SendMessage(chatId, answer.messageText())
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(inlineKeyboard));
        }
    }

    private InlineKeyboardMarkup createInlineKeyboard(List<String> words) {
        if (words.get(0).contains(COMMAND_EXAMPLE_SUFFIX)) {
            return new InlineKeyboardMarkup(
                    new InlineKeyboardButton[][]{
                            {new InlineKeyboardButton("Мад меселаяр къалурун").callbackData(words.get(0))}
                    }
            );
        }
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (String word : words) {
            List<InlineKeyboardButton> buttonList = new ArrayList<>();
            buttonList.add(new InlineKeyboardButton(word).callbackData(word));
            buttons.add(buttonList);
        }
        InlineKeyboardButton[][] inlineKeyboardButton = new InlineKeyboardButton[buttons.size()][1];
        for (int i = 0; i < inlineKeyboardButton.length; i++) {
            for (int j = 0; j < inlineKeyboardButton[i].length; j++) {
                inlineKeyboardButton[i][j] = buttons.get(i).get(j);
            }
        }
        return new InlineKeyboardMarkup(inlineKeyboardButton);
    }
}