package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendVoice;
import core.config.Env;
import core.ui.InlineKeyboardCreator;
import core.utils.AlphabetBuilder;
import lombok.AllArgsConstructor;

import java.io.File;
import java.net.URL;

import static core.commands.CommandsList.*;

@AllArgsConstructor
public class ResponseFromAlphabetCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;
    private final String commandKey;
    private final String letter;

    @Override
    public void execute() {
        var chatId = message.chat().id();
        AlphabetBuilder alphabet = new AlphabetBuilder();
        String outputMsg = "<b>" + letter + " — " + alphabet.get(letter) + "</b>";
        if (letter.equals("Ь") || letter.equals("Ъ")) {
            bot.execute(new SendMessage(chatId, outputMsg).parseMode(ParseMode.HTML));
            return;
        }
        if (commandKey.equals(ALPHABET)) {
            InlineKeyboardMarkup keyboard = InlineKeyboardCreator.createAlphabetAudioButton(letter);
            bot.execute(new SendMessage(chatId, outputMsg)
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(keyboard));
        } else if (commandKey.equals(AUDIO_ALPHABET)) {
            String audioFilePath = getAudioFilePath(letter);
            File audioFile = new File(audioFilePath);
            bot.execute(new SendVoice(chatId, audioFile));
        }
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