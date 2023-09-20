package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import core.parser.DictionaryRepository;
import core.parser.model.Definition;
import core.parser.model.DefinitionDetails;
import core.parser.model.Example;
import core.parser.model.ExpressionDetails;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static core.BotUpdates.*;

import static core.StringSimilarity.similarity;
import static core.commands.SendResponseFromButtonCommandProcessor.COMMAND_EXAMPLE_SUFFIX;

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
        var userMessage = message.text();
        var language = selectedLanguage.getDictionaryLanguage(chatId);
        switch (language) {
            case CommandsList.LEZGI_RUS -> sendAnswerOfFoundWord(lezgiRusDictionary, userMessage, chatId);
            case CommandsList.RUS_LEZGI -> sendAnswerOfFoundWord(rusLezgiDictionary, userMessage, chatId);
            default -> {
            }
        }
    }

    public void sendAnswerOfFoundWord(DictionaryRepository dictionary, String spelling, long chatId) {
        String userMessage = spelling.substring(0, 1).toUpperCase() + spelling.substring(1).toLowerCase()
                .replaceAll("[i1lӏ|]", "I");
        int amountOfValues = 1;
        boolean generalExample = false;
        StringBuilder tags = new StringBuilder();
        StringBuilder outputMessage = new StringBuilder();
        try {
            List<ExpressionDetails> expressionDetails = dictionary.getDefinitions(spelling);
            for (ExpressionDetails details : expressionDetails) {
                /* Если слово есть в словаре, но отсутствуют переводы */
                if (details.getDefinitionDetails().isEmpty() && details.getExamples() == null) {
                    bot.execute(new SendMessage(chatId, "<b>❌Таржума жагъанач</b>").parseMode(ParseMode.HTML));
                    return;
                }
                /* Если к слову отсутвуют переводы, есть только examples, вывести example */
                if ((details.getDefinitionDetails().size() == 0 || details.getDefinitionDetails() == null)
                        && details.getExamples() != null) {
                    sendOnlyExamples(chatId, userMessage, outputMessage, details);
                    return;
                }
                /* Если у слова несколько или одно значений */
                markupExpressionsSpelling(userMessage, amountOfValues, outputMessage, expressionDetails, details);
                for (DefinitionDetails definitionDetails : details.getDefinitionDetails()) {
                    StringBuilder values = new StringBuilder();
                    for (Definition definitions : definitionDetails.getDefinitions()) {
                        boolean isTagNull = definitions.getTags() == null;
                        tags.append(!isTagNull && definitions.getTags().contains("см.")
                                ? definitions.getValue().replaceAll("\\{", "").replaceAll("}", "") + ", "
                                : ""
                        );
                        values.append(isTagNull || !definitions.getTags().contains("см.")
                                ? definitions.getValue().replaceAll("\\{", "[").replaceAll("}", "]") + ", "
                                : "");
                    }
                    if (!values.isEmpty()) {
                        /* Присваиваем value и удаляем запятую на конце */
                        outputMessage
                                .append("➡️<b>️ ")
                                .append(values.deleteCharAt(values.length() - 2))
                                .append("</b>\n\n");
                    }
                    if (definitionDetails.getExamples() != null) {
                        definitionDetails.getExamples().forEach(example -> {
                            outputMessage.append("   - ").append(example.getRaw()
                                    .replaceAll("<", "[")
                                    .replaceAll(">", "]")
                                    .replaceAll("\\{", "<i>")
                                    .replaceAll("}", "</i> - ")).append("\n");
                        });
                        outputMessage.append("\n");
                    }
                }
                if (!generalExample) { /* Проверяем наличие общего example */
                    generalExample = details.getExamples() != null;
                }
                amountOfValues++;
            }
            /* Если теги есть, присваиваем к ответу */
            outputMessage.append(!tags.isEmpty()
                    ? "\n<i>см. тж.: </i>" + tags.deleteCharAt(tags.length() - 2)
                    : "");
            /* Создаем кнопку для вывода общих примеров */
            if (generalExample) {
                InlineKeyboardButton[][] inlineKeyboardButton = {
                        {new InlineKeyboardButton("Мад меселаяр къалурун").callbackData(userMessage.toLowerCase() + COMMAND_EXAMPLE_SUFFIX)}
                };
                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(inlineKeyboardButton);
                bot.execute(new SendMessage(chatId, outputMessage.toString()).parseMode(ParseMode.HTML)
                        .replyMarkup(inlineKeyboard));
                return;
            }
            bot.execute(new SendMessage(chatId, outputMessage.toString()).parseMode(ParseMode.HTML));
        } catch (Exception e) {
            e.printStackTrace();
            sendAnswerWithSuggestedWords(dictionary, spelling, chatId);
        }
    }

    private void markupExpressionsSpelling(String userMessage, int amountOfValues, StringBuilder outputMessage,
                                           List<ExpressionDetails> expressionDetails, ExpressionDetails details) {
        if (expressionDetails.size() > 1) {
            outputMessage.append(details.getInflection() != null ?
                    "<i>" + amountOfValues + ". " + userMessage + " (" + details.getInflection() + ")</i> ⤵️\n\n"
                    : "<i>" + amountOfValues + ". " + userMessage + "️</i> ⤵️️\n\n");
        } else {
            outputMessage.append(details.getInflection() != null ?
                    "<i>" + userMessage + " (" + details.getInflection() + ")</i> ⤵️\n\n"
                    : "<i>" + userMessage + "</i> ⤵️\n\n");
        }
    }

    private void sendOnlyExamples(long chatId, String userMessage, StringBuilder outputMessage, ExpressionDetails details) {
        outputMessage.append("<i>").append(userMessage).append("️</i> ⤵️\n\n");
        details.getExamples().forEach(example -> {
            outputMessage.append("   - ").append(example.getRaw()
                    .replaceAll("<", "[")
                    .replaceAll(">", "]")
                    .replaceAll("\\{", "<b><i>")
                    .replaceAll("}", "</i></b> - ")).append("\n");
        });
        bot.execute(new SendMessage(chatId, outputMessage.toString()).parseMode(ParseMode.HTML));
    }

    private void sendAnswerWithSuggestedWords(DictionaryRepository dictionary, String userMessage, long chatId) {
        if (sendAnswerFromExamples(userMessage, chatId)) {
            return;
        }
        record WordSim(String supposedWord, Double sim) {
        }
        List<WordSim> wordList = new ArrayList<>();
        for (String supposedWord : dictionary.getFullDictionary().keySet()) {
            double sim = similarity(supposedWord, userMessage);
            if (sim >= 0.5) {
                wordList.add(new WordSim(supposedWord.replaceAll("i", "I"), sim));
            }
        }
        wordList.sort(Comparator.comparing(WordSim::sim).reversed());
        if (wordList.subList(0, Math.min(7, wordList.size())).size() == 0) {
            bot.execute(new SendMessage(chatId, "<b>❌Жагъай гаф авач</b>").parseMode(ParseMode.HTML));
            return;
        }
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (var wordSim : wordList.subList(0, Math.min(7, wordList.size()))) {
            List<InlineKeyboardButton> buttonList = new ArrayList<>();
            buttonList.add(new InlineKeyboardButton(wordSim.supposedWord).callbackData(wordSim.supposedWord));
            buttons.add(buttonList);
        }
        InlineKeyboardButton[][] inlineKeyboardButton = new InlineKeyboardButton[buttons.size()][1];
        for (int i = 0; i < inlineKeyboardButton.length; i++) {
            for (int j = 0; j < inlineKeyboardButton[i].length; j++) {
                inlineKeyboardButton[i][j] = buttons.get(i).get(j);
            }
        }
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(inlineKeyboardButton);
        bot.execute(new SendMessage(chatId, "\uD83E\uDD14Жагъай затI хьанач, и гафариз килиг:\n")
                .replyMarkup(inlineKeyboard));
    }

    private boolean sendAnswerFromExamples(String userMessage, long chatId) {
        String inputMessage = userMessage.toLowerCase().replaceAll("[i1lӏ|]", "I");
        StringBuilder outputMessage = new StringBuilder("<i>"
                + inputMessage.substring(0, 1).toUpperCase()
                + inputMessage.substring(1)
                + "</i> ⤵️️️\n\n");
        String wordToFind = "\\b" + Pattern.quote(inputMessage) + "\\b";
        Pattern pattern = Pattern.compile(wordToFind, Pattern.UNICODE_CHARACTER_CLASS);
        int numberOfExamplesFound = 0;
        for (Example examples : listOfExample) {
            Matcher matcher = pattern.matcher(examples.getRaw());
            if (matcher.find() && numberOfExamplesFound < 10) {
                outputMessage.append("   - ").append(examples.getRaw()
                        .replaceAll("<", "[")
                        .replaceAll(">", "]")
                        .replaceAll(inputMessage, "<u>" + inputMessage + "</u>")
                        .replaceAll("\\{", "<b><i>")
                        .replaceAll("}", "</i></b> -")).append("\n");
                numberOfExamplesFound++;
            }
        }
        if (numberOfExamplesFound > 0) {
            bot.execute(new SendMessage(chatId, outputMessage.toString()).parseMode(ParseMode.HTML));
            return true;
        }
        return false;
    }
}