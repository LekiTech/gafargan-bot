package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import core.dictionary.model.DialectDictionary;
import core.dictionary.parser.DictionaryRepository;
import core.searchers.FuzzySearchBySpelling;
import core.searchers.NumbersSearchResponseHandler;
import core.searchers.Response;
import core.searchers.SearchResponseHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static core.commands.CommandsList.*;
import static core.searchers.StringSimilarity.similarity;
import static core.ui.InlineKeyboardCreator.createInlineKeyboard;
import static core.utils.OutputLineEditor.insertAuthorsName;
import static core.utils.SearchStringNormalizer.normalizeString;
import static core.utils.WordCapitalize.capitalizeFirstLetter;

public class ResponseSearchCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final DictionaryRepository dictionaries;
    private final TelegramBot bot;
    private final String lang;

    public ResponseSearchCommandProcessor(Message message, DictionaryRepository dictionaries, TelegramBot bot, String lang) {
        this.message = message;
        this.dictionaries = dictionaries;
        this.bot = bot;
        this.lang = lang;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        var userMessage = normalizeString(message.text());
        SearchResponseHandler messageHandler = new SearchResponseHandler(bot);
        NumbersSearchResponseHandler numbersHandler = new NumbersSearchResponseHandler(bot);
        switch (lang) {
            case LEZGI_RUS -> messageHandler.sendResponse(LEZ, dictionaries, userMessage, chatId);
            case RUS_LEZGI -> messageHandler.sendResponse(RUS, dictionaries, userMessage, chatId);
            case LEZGI_ENG -> sendResponseFromLezgiEngDict(dictionaries, userMessage, chatId);
            case LEZGI_DIALECT_DICT -> sendResponseFromDialectDict(dictionaries, userMessage, chatId);
            case LEZGI_NUMBERS -> numbersHandler.findResponse(userMessage, chatId);
            default -> {
            }
        }
    }

    /* Temporary code due to the fact that the JSON format of the Lezgi-English dictionary is different from other dictionaries. */
    public void sendResponseFromLezgiEngDict(DictionaryRepository dictionaries, String userMessage, Long chatId) {
        List<String> definitions = dictionaries.getFromLezEngDictionary(userMessage);

        if (definitions.isEmpty() || definitions == null) {
            record WordSim(String supposedWord, Double sim) {
            }
            final List<WordSim> wordList = dictionaries.getLezgiEngDict().keySet().stream()
                    .parallel()
                    .map(supposedWord -> new WordSim(supposedWord.replaceAll("i", "I"), similarity(supposedWord, userMessage.toLowerCase())))
                    .filter(wordSim -> wordSim.sim() >= 0.5)
                    .sorted(Comparator.comparing(WordSim::sim).reversed())
                    .limit(7)
                    .toList();
            if (wordList.isEmpty()) {
                bot.execute(new SendMessage(chatId, "<b>❌Гаф жагъанач</b>\n" + insertAuthorsName(ENG))
                        .parseMode(ParseMode.HTML));
                return;
            }
            final List<String> supposedWords = wordList.stream()
                    .map(WordSim::supposedWord)
                    .toList();
            InlineKeyboardMarkup inlineKeyboard = createInlineKeyboard(supposedWords);
            bot.execute(new SendMessage(chatId, "\uD83E\uDD14гаф жагъанач, ибуруз килиг:\n")
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(inlineKeyboard));
            return;
        }
        StringBuilder outputMsg = new StringBuilder(capitalizeFirstLetter(userMessage));
        for (String definition : definitions) {
            outputMsg.append(definition);
        }
        outputMsg.append("\n\n").append(insertAuthorsName(ENG));
        bot.execute(new SendMessage(chatId, outputMsg.toString())
                .parseMode(ParseMode.HTML));
    }

    public void sendResponseFromDialectDict(DictionaryRepository dictionaries, String userMessage, Long chatId) {
        StringBuilder outputMsg = new StringBuilder();
        Map<String, List<DialectDictionary.Dialect>> dialectDictionary = dictionaries.getDialectDictionary();
        if (dialectDictionary.get(userMessage) != null) {
            outputMsg.append("<b><i>- литератур. нугъат:</i></b> ").append(userMessage).append("\n");
            for (DialectDictionary.Dialect dialect : dialectDictionary.get(userMessage)) {
                outputMsg.append("<b><i>- ").append(dialect.getName()).append(":</i></b> ").append(dialect.getSpelling()).append("\n");
            }
            outputMsg.append("\n").append("<i>⛰\"Нугъатдин гафарган\" Гайдаров Р.И.</i>");
            bot.execute(new SendMessage(chatId, outputMsg.toString())
                    .parseMode(ParseMode.HTML));
        } else if (dialectDictionary.get(userMessage) == null) {
            boolean flag = false;
            for (Map.Entry<String, List<DialectDictionary.Dialect>> entry : dialectDictionary.entrySet()) {
                for (DialectDictionary.Dialect dialect : entry.getValue()) {
                    if (dialect.getSpelling().equals(userMessage)) {
                        outputMsg.append("<b><i>- литератур. нугъат:</i></b> ").append(entry.getKey()).append("\n");
                        for (DialectDictionary.Dialect word : dialectDictionary.get(entry.getKey())) {
                            outputMsg.append("<b><i>- ").append(word.getName()).append(":</i></b> ").append(word.getSpelling()).append("\n");
                        }
                        outputMsg.append("\n").append("<i>⛰\"Нугъатдин гафарган\" Гайдаров Р.И.</i>");
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    break;
                }
            }
            if (outputMsg.isEmpty()) {
                record WordSim(String supposedWord, Double sim) {
                }
                final List<WordSim> wordList = dialectDictionary.keySet().stream()
                        .parallel()
                        .map(supposedWord -> new WordSim(supposedWord, similarity(supposedWord, userMessage)))
                        .filter(wordSim -> wordSim.sim() >= 0.5)
                        .sorted(Comparator.comparing(WordSim::sim).reversed())
                        .limit(7)
                        .toList();
                if (wordList.isEmpty()) {
                    bot.execute(new SendMessage(chatId, "<b>❌Гаф жагъанач</b>\n" + "<i>⛰\"Нугъатдин гафарган\" Гайдаров Р.И.</i>")
                            .parseMode(ParseMode.HTML));
                }
                final List<String> supposedWords = wordList.stream()
                        .map(WordSim::supposedWord)
                        .toList();
                InlineKeyboardMarkup inlineKeyboard = createInlineKeyboard(supposedWords);
                bot.execute(new SendMessage(chatId, "\uD83E\uDD14гаф жагъанач, ибуруз килиг:\n")
                        .parseMode(ParseMode.HTML)
                        .replyMarkup(inlineKeyboard));
            } else {
                bot.execute(new SendMessage(chatId, outputMsg.toString())
                        .parseMode(ParseMode.HTML));
            }
        }
    }
}