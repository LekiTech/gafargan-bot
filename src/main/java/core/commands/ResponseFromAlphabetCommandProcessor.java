package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendVoice;
import core.config.Env;
import core.utils.AlphabetBuilder;

import java.io.File;
import java.net.URL;

public class ResponseFromAlphabetCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;
    private final CallbackQuery callbackQuery;

    public ResponseFromAlphabetCommandProcessor(Message message, TelegramBot bot, CallbackQuery callbackQuery) {
        this.message = message;
        this.bot = bot;
        this.callbackQuery = callbackQuery;
    }

    @Override
    public void execute() {
        var chatId = message.chat().id();
        var userMessage = callbackQuery.data();
        AlphabetBuilder alphabet = new AlphabetBuilder();
        String outputMsg = "<b>" + userMessage + " — " + alphabet.get(userMessage) + "</b>";
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