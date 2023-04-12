package core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.*;

import static core.StringSimilarity.similarity;

public class Main {
    private final static String START = "/start";
    private final static String LEZGI_RUS_BABAKHANOV = "\uD83D\uDCD7лезгинско-русский словарь (бабаханов м.б.)";
    private final static String LEZGI_RUS_TALIBOV_HAJIYEV = "\uD83D\uDCD9лезгинско-русский словарь (талибов б., гаджиев м.)";
    private final static String RUS_LEZGI = "\uD83D\uDCD5русско-лезгинский словарь (гаджиев м.м.)";
    private final static String INFO = "/info";

    public static void main(String[] args) throws IOException {
        System.out.println("Starting the bot...");
        Dotenv dotenv = null;
        dotenv = Dotenv.configure().load();
        var token = dotenv.get("TELEGRAM_API_TOKEN");
        if (token == null) {
            System.err.println("Telegram token not found");
            return;
        }
        TelegramBot bot = new TelegramBot(token);

        ParsedDictionary rusLezgiDict = new ParsedDictionary();
        rusLezgiDict.parse("rus_lezgi_dict_hajiyev.json");
        ParsedDictionary lezgiRusDictBabakhanov = new ParsedDictionary();
        lezgiRusDictBabakhanov.parse("lezgi_rus_dict_babakhanov.json");
        ParsedDictionary lezgiRusDictTalibovHajiyev = new ParsedDictionary();
        lezgiRusDictTalibovHajiyev.parse("lezgi_rus_dict_talibov_hajiyev.json");

        Map<Long, String> dictionaryLanguage = new HashMap<>();
        Map<String, String> clickButtLezgiBabakhanov = new HashMap<>();
        Map<String, String> clickButtLezgiTalibovHajiyev = new HashMap<>();
        Map<String, String> clickButtonsRus = new HashMap<>();

        bot.setUpdatesListener(updates -> {
            try {
                for (var update : updates) {
                    /* Обработка buttons */
                    if (update.callbackQuery() != null) {
                        CallbackQuery callbackQuery = update.callbackQuery();
                        Message buttonMessage = callbackQuery.message();
                        long chatId = buttonMessage.chat().id();
                        String data = callbackQuery.data();
                        if (clickButtLezgiBabakhanov.containsKey(data)
                                && LEZGI_RUS_BABAKHANOV.equals(clickButtLezgiBabakhanov.get(data))) {
                            sendAnswerFromButtons(bot, lezgiRusDictBabakhanov, callbackQuery, chatId, data);
                        } else if (clickButtLezgiTalibovHajiyev.containsKey(data)
                                && LEZGI_RUS_TALIBOV_HAJIYEV.equals(clickButtLezgiTalibovHajiyev.get(data))) {
                            sendAnswerFromButtons(bot, lezgiRusDictTalibovHajiyev, callbackQuery, chatId, data);
                        } else if (clickButtonsRus.containsKey(data)
                                && RUS_LEZGI.equals(clickButtonsRus.get(data))) {
                            sendAnswerFromButtons(bot, rusLezgiDict, callbackQuery, chatId, data);
                        }
                    }
                    var message = update.message();
                    if (message == null) {
                        continue;
                    }
                    long chatId = message.chat().id();
                    String textMsg = message.text();
                    if (textMsg == null) {
                        continue;
                    }
                    /* Обработка команд */
                    String userMessage = textMsg.toLowerCase().replaceAll("1", "i");
                    if (START.equals(userMessage)) {
                        var sendMessage = new SendMessage(chatId, "Ас-саляму алейкум\uD83D\uDC4B\uD83C\uDFFC\n\n" +
                                "Вун атуй, рагъ атуй!\nДобро пожаловать!");
                        sendMessage.parseMode(ParseMode.HTML);
                        bot.execute(sendMessage);
                        ReplyKeyboardMarkup keypad = new ReplyKeyboardMarkup(
                                new KeyboardButton[]{
                                        new KeyboardButton("\uD83D\uDCD9Лезгинско-русский словарь (Талибов Б., Гаджиев М.)")
                                },
                                new KeyboardButton[]{
                                        new KeyboardButton("\uD83D\uDCD7Лезгинско-русский словарь (Бабаханов М.Б.)")
                                },
                                new KeyboardButton[]{
                                        new KeyboardButton("\uD83D\uDCD5Русско-лезгинский словарь (Гаджиев М.М.)")
                                }
                        );
                        keypad.resizeKeyboard(true);
                        bot.execute(new SendMessage(chatId, "\uD83D\uDCDAВыберите словарь:").replyMarkup(keypad));
                    } else if (INFO.equals(userMessage)) {
                        String txt = "Разработчик\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB: Артур Магомедов.\n\n"
                                + "Бот-словарь\uD83D\uDCDA, переводит слова с Лезгинского языка на русский и наоборот. "
                                + "Также бот умеет находить слова, если пользователь вводит их с орфографическими ошибками.\n\n"
                                + "Используемые словари:\n"
                                + "- Лезгинско-русский словарь\uD83D\uDCD9 (Б.Талибов, М.Гаджиев)\n"
                                + "- Русско-лезгинский словарь\uD83D\uDCD5 (М.М.Гаджиев)\n"
                                + "- Лезгинско-русский словарь\uD83D\uDCD7 (Бабаханов М.Б.)";
                        bot.execute(new SendMessage(chatId, txt));
                    } else if (RUS_LEZGI.equals(userMessage)) {
                        dictionaryLanguage.put(chatId, RUS_LEZGI);
                        bot.execute(new SendMessage(chatId, "Выбран \uD83D\uDCD5Русско-лезгинский словарь (Гаджиев М.М.)"));
                    } else if (LEZGI_RUS_BABAKHANOV.equals(userMessage)) {
                        dictionaryLanguage.put(chatId, LEZGI_RUS_BABAKHANOV);
                        bot.execute(new SendMessage(chatId, "Выбран \uD83D\uDCD7Лезгинско-русский словарь (Бабаханов М.Б.)\n\n"
                                + "Буквы с палочкой (кI, тI, пI...) вводите через единицу «1» или латинкую букву «i»."));
                    } else if (LEZGI_RUS_TALIBOV_HAJIYEV.equals(userMessage)) {
                        dictionaryLanguage.put(chatId, LEZGI_RUS_TALIBOV_HAJIYEV);
                        bot.execute(new SendMessage(chatId, "Выбран \uD83D\uDCD9Лезгинско-русский словарь (Талибов Б., Гаджиев М.)\n\n"
                                + "Буквы с палочкой (кI, тI, пI...) вводите через единицу «1» или латинкую букву «i»."));
                    }
                    if (RUS_LEZGI.equals(dictionaryLanguage.get(chatId))
                            && (rusLezgiDict.map.containsKey(userMessage))) {
                        sendAnswerFromDictionary(bot, rusLezgiDict, chatId, userMessage);
                    } else if (LEZGI_RUS_BABAKHANOV.equals(dictionaryLanguage.get(chatId))
                            && (lezgiRusDictBabakhanov.map.containsKey(userMessage))) {
                        sendAnswerFromDictionary(bot, lezgiRusDictBabakhanov, chatId, userMessage);
                    } else if (LEZGI_RUS_TALIBOV_HAJIYEV.equals(dictionaryLanguage.get(chatId))
                            && (lezgiRusDictTalibovHajiyev.map.containsKey(userMessage))) {
                        sendAnswerFromDictionary(bot, lezgiRusDictTalibovHajiyev, chatId, userMessage);
                    } else if (LEZGI_RUS_BABAKHANOV.equals(dictionaryLanguage.get(chatId))) {
                        sendAnswerToUserWhenIncorrectInput(bot, lezgiRusDictBabakhanov, clickButtLezgiBabakhanov, chatId, userMessage,
                                LEZGI_RUS_BABAKHANOV);
                    } else if (LEZGI_RUS_TALIBOV_HAJIYEV.equals(dictionaryLanguage.get(chatId))) {
                        sendAnswerToUserWhenIncorrectInput(bot, lezgiRusDictTalibovHajiyev, clickButtLezgiTalibovHajiyev, chatId, userMessage,
                                LEZGI_RUS_TALIBOV_HAJIYEV);
                    } else if (RUS_LEZGI.equals(dictionaryLanguage.get(chatId))) {
                        sendAnswerToUserWhenIncorrectInput(bot, rusLezgiDict, clickButtonsRus, chatId, userMessage, RUS_LEZGI);
                        /*  Когда ни один словарь не выбран */
                    } else if (dictionaryLanguage.isEmpty()
                            && !START.equals(userMessage)
                            && !INFO.equals(userMessage)
                            && !RUS_LEZGI.equals(userMessage)
                            && !LEZGI_RUS_BABAKHANOV.equals(userMessage)
                            && !LEZGI_RUS_TALIBOV_HAJIYEV.equals(userMessage)) {
                        bot.execute(new SendMessage(chatId, "Пожалуйста, сначала выберите словарь\uD83D\uDCDA"));
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private static void sendAnswerFromDictionary(TelegramBot bot, ParsedDictionary dictionary, long chatId,
                                                 String userMessage) {
        List<String> temp = dictionary.map.get(userMessage);
        var translations = temp.subList(0, Math.min(7, temp.size()));
        String msgStr = convertToHtml(translations);
        var sendMessage = new SendMessage(chatId, msgStr);
        sendMessage.parseMode(ParseMode.HTML);
        bot.execute(sendMessage);
    }

    protected static void sendAnswerFromButtons(TelegramBot bot, ParsedDictionary dictionary, CallbackQuery callbackQuery,
                                                long chatId, String data) {
        List<String> temp = dictionary.map.get(data.toLowerCase());
        var translations = temp.subList(0, Math.min(7, temp.size()));
        String msgStr = convertToHtml(translations);
        var sendMessage = new SendMessage(chatId, msgStr);
        sendMessage.parseMode(ParseMode.HTML);
        bot.execute(new SendMessage(chatId, data.toUpperCase() + "⬇️:"));
        bot.execute(sendMessage);
        bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
    }

    private static void sendAnswerToUserWhenIncorrectInput(TelegramBot bot, ParsedDictionary dictionary,
                                                           Map<String, String> clickButtons,
                                                           long chatId, String userMessage, String language) {
        if (START.equals(userMessage)
                || INFO.equals(userMessage)
                || RUS_LEZGI.equals(userMessage)
                || LEZGI_RUS_BABAKHANOV.equals(userMessage)
                || LEZGI_RUS_TALIBOV_HAJIYEV.equals(userMessage)) {
            return;
        }
        record WordSim(String word, Double sim) {
        }
        List<WordSim> temp = new ArrayList<>();
        for (String word : dictionary.map.keySet()) {
            double sim = similarity(word, userMessage);
            if (sim >= 0.5) {
                temp.add(new WordSim(word.toLowerCase().replaceAll("i", "I"), sim));
            }
        }
        temp.sort(Comparator.comparing(WordSim::sim).reversed());
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (var wordSim : temp.subList(0, Math.min(7, temp.size()))) {
            List<InlineKeyboardButton> tempList = new ArrayList<>();
            tempList.add(new InlineKeyboardButton(wordSim.word).callbackData(wordSim.word));
            buttons.add(tempList);
            clickButtons.put(wordSim.word, language);
        }
        InlineKeyboardButton[][] inlineKeyboardButton = new InlineKeyboardButton[buttons.size()][1];
        for (int i = 0; i < inlineKeyboardButton.length; i++) {
            for (int j = 0; j < inlineKeyboardButton[i].length; j++) {
                inlineKeyboardButton[i][j] = buttons.get(i).get(j);
            }
        }
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(inlineKeyboardButton);
        if (clickButtons.size() > 0 && temp.size() > 0) {
            bot.execute(new SendMessage(chatId, "Ничего не нашлось\uD83E\uDD14, возможно вы имели ввиду:\n")
                    .replyMarkup(inlineKeyboard));
        } else {
            bot.execute(new SendMessage(chatId, "Ничего не нашлось\uD83E\uDD72. Повторите запрос"));
        }
    }

    private static String convertToHtml(List<String> translations) {
        String msgStr = String.join("\n\n", translations).replaceAll(">", ">>>")
                .replaceAll("<", "<i>")
                .replaceAll(">>>", "</i>")
                .replaceAll("\\{", "<b>")
                .replaceAll("}", "</b>");
        return msgStr;
    }
}