package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendVoice;
import core.config.Env;

import java.io.File;
import java.net.URL;
import java.util.*;

public class ResponseFromAlphabetCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;
    private final CallbackQuery callbackQuery;
    private final Map<String, String> map = new HashMap<>();

    {
        map.put("А", "аялар\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66");
        map.put("Б", "бацIи\uD83D\uDC10");
        map.put("В", "вирт\uD83C\uDF6F");
        map.put("Г", "гурцIул\uD83D\uDC36");
        map.put("Гъ", "гъед\uD83D\uDC20");
        map.put("Гь", "гьер\uD83D\uDC0F");
        map.put("Д", "дакIар\uD83E\uDE9F");
        map.put("Е", "еб\uD83E\uDEA2");
        map.put("Ж", "жив❄️");
        map.put("З", "зарар\uD83C\uDFB2");
        map.put("И", "ич\uD83C\uDF4F");
        map.put("Й", "йиф\uD83C\uDF19");
        map.put("К", "кац\uD83D\uDC08");
        map.put("Къ", "къаз\uD83E\uDEBF");
        map.put("Кь", "кьиф\uD83D\uDC01");
        map.put("КI", "кIвал\uD83C\uDFE0");
        map.put("Л", "лекь\uD83E\uDD85");
        map.put("М", "муьгъ\uD83C\uDF09");
        map.put("Н", "нуькI\uD83D\uDC26\u200D⬛️");
        map.put("П", "пеш\uD83C\uDF42");
        map.put("ПI", "пIини\uD83C\uDF52");
        map.put("Р", "рагъ☀️");
        map.put("С", "сикI\uD83E\uDD8A");
        map.put("Т", "тар\uD83C\uDF34");
        map.put("ТI", "тIур\uD83E\uDD44");
        map.put("У", "улуб\uD83D\uDCD8");
        map.put("Уь", "уьтуь");
        map.put("Ф", "фу\uD83C\uDF5E");
        map.put("Х", "хват");
        map.put("Хъ", "хъвер\uD83D\uDE42");
        map.put("Хь", "хьел\uD83C\uDFF9");
        map.put("Ц", "ципицIар\uD83C\uDF47");
        map.put("ЦI", "цIай\uD83D\uDD25");
        map.put("Ч", "чичIек\uD83E\uDDC5");
        map.put("ЧI", "чIиж\uD83D\uDC1D");
        map.put("Ш", "шив\uD83D\uDC0E");
        map.put("Ъ", "кIеви лишан");
        map.put("Ы", "ы");
        map.put("Ь", "хъуьтуьл лишан");
        map.put("Э", "экв\uD83D\uDCA1");
        map.put("Ю", "югъ\uD83C\uDFD9");
        map.put("Я", "яргъируш");
    }

    public ResponseFromAlphabetCommandProcessor(Message message, TelegramBot bot, CallbackQuery callbackQuery) {
        this.message = message;
        this.bot = bot;
        this.callbackQuery = callbackQuery;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        var userMessage = callbackQuery.data();
        String outputMsg = "<b>" + userMessage + "</b> — <b>" + map.get(userMessage) + "</b>";
        if (userMessage.equals("Ь") || userMessage.equals("Ъ")) {
            bot.execute(new SendMessage(chatId, outputMsg).parseMode(ParseMode.HTML));
            bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
            return;
        }
        String audioFilePath = getAudioFilePath(userMessage);
        File audioFile = new File(audioFilePath);
        bot.execute(new SendMessage(chatId, outputMsg).parseMode(ParseMode.HTML));
        bot.execute(new SendVoice(chatId, audioFile));
        bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
    }

    private String getAudioFilePath(String userMessage) {
        String audioFilePath = "/alphabet/" + userMessage + ".mp3";
        try {
            if (Env.isRunningFromJar()) {
                File jarPath = new File(ResponseFromAlphabetCommandProcessor.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                File audioFile = new File(jarPath.getParent(), audioFilePath);
                if (!audioFile.exists()) {
                    System.err.println("Audio file not found: " + audioFile);
                    return null;
                }
                return audioFile.getAbsolutePath();
            } else {
                URL resource = ResponseFromAlphabetCommandProcessor.class.getResource(audioFilePath);
                assert resource != null;
                return resource.toURI().getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}