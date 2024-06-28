package core.searchers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import core.ui.InlineKeyboardCreator;
import io.lekitech.LezgiNumbers;

import java.math.BigInteger;

import static core.utils.WordCapitalize.capitalizeFirstLetter;

public class NumeralSearchResponseHandler {

    private final TelegramBot bot;

    public NumeralSearchResponseHandler(TelegramBot bot) {
        this.bot = bot;
    }

    public void findResponse(String userMessage, Long chatId) {
        if (isNumber(userMessage)) {
            sendNumeralAsText(userMessage, chatId);
        } else {
            sendNumeralAsNumbers(userMessage, chatId);
        }
    }

    private void sendNumeralAsText(String userMessage, Long chatId) {
        try {
            String numToLezgi = LezgiNumbers.numToLezgi(new BigInteger(userMessage));
            var button = InlineKeyboardCreator.createNumeralAudioButton(userMessage);
            String outputMsg = capitalizeFirstLetter(userMessage)
                               + "➡️ <code>" + numToLezgi + "</code>";
            bot.execute(new SendMessage(chatId, outputMsg)
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(button));
        } catch (Exception e) {
            bot.execute(new SendMessage(chatId, "<b>❌Числительное жагъанач.</b>\n      <i>Дуьз кхьихь, месела: 10, -444, 2, ... ва икI мад</i>")
                    .parseMode(ParseMode.HTML));
        }
    }

    private void sendNumeralAsNumbers(String userMessage, Long chatId) {
        try {
            String lezgiToNum = String.valueOf(LezgiNumbers.lezgiToNum(userMessage));
            var button = InlineKeyboardCreator.createNumeralAudioButton(lezgiToNum);
            String outputMsg = capitalizeFirstLetter(userMessage)
                               + "➡️ <code>" + lezgiToNum + "</code>";
            bot.execute(new SendMessage(chatId, outputMsg)
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(button));
        } catch (Exception e) {
            bot.execute(new SendMessage(chatId, "<b>❌Числительное жагъанач.</b>\n      <i>Дуьз кхьихь, месела: вад вишни къанни пуд, ... ва икI мад</i>")
                    .parseMode(ParseMode.HTML));
        }
    }

    private boolean isNumber(String message) {
        return message.toLowerCase().equals(message.toUpperCase());
    }
}