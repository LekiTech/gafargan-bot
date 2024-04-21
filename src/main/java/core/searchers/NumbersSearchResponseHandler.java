package core.searchers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendVoice;
import core.config.Env;
import io.lekitech.LezgiNumbers;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static core.utils.WordCapitalize.capitalizeFirstLetter;
import static io.lekitech.LezgiNumbers.numToLezgiTTS;

public class NumbersSearchResponseHandler {

    private final TelegramBot bot;

    public NumbersSearchResponseHandler(TelegramBot bot) {
        this.bot = bot;
    }

    public void findResponse(String userMessage, Long chatId) {
        if (isNumber(userMessage)) {
            sendTextAndAudioResponse(userMessage, chatId);
        } else {
            try {
                String outputMessage = capitalizeFirstLetter(userMessage)
                                       + "➡️ <code>"
                                       + LezgiNumbers.lezgiToNum(userMessage)
                                       + "</code>";
                bot.execute(new SendMessage(chatId, outputMessage).parseMode(ParseMode.HTML));
            } catch (Exception e) {
                bot.execute(new SendMessage(chatId, "<b>❌Таржума жагъанач</b>").parseMode(ParseMode.HTML));
            }
        }
    }

    private void sendTextAndAudioResponse(String userMessage, Long chatId) {
        try {
            List<Byte> listOfBytes = new ArrayList<>();
            List<String> audios = LezgiNumbers.numToLezgiList(new BigInteger(userMessage));
            audios.forEach(el -> {
                String sound = el.equals(" ") ? "/audio/_.wav"
                        : String.format("/audio/%s.wav", el);
                try {
                    String path = getAudioFilePath(sound);
                    assert path != null;
                    byte[] audioBytes = numToLezgiTTS(path);
                    for (byte audio : audioBytes) {
                        listOfBytes.add(audio);
                    }
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            });
            byte[] allAudioBytes = new byte[listOfBytes.size()];
            for (int i = 0; i < allAudioBytes.length; i++) {
                allAudioBytes[i] = listOfBytes.get(i);
            }
            // Создание временного аудиофайла
            File tempFile = File.createTempFile("audio", ".wav");
            // Создание аудиофайла
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    44100, 16, 2, 4, 44100, false);
            ByteArrayInputStream bais = new ByteArrayInputStream(allAudioBytes);
            AudioInputStream ais = new AudioInputStream(bais, format, allAudioBytes.length / format.getFrameSize());
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, tempFile);
            String outputMessage = capitalizeFirstLetter(userMessage)
                                   + "➡️ <code>"
                                   + LezgiNumbers.numToLezgi(new BigInteger(userMessage))
                                   + "</code>";
            bot.execute(new SendMessage(chatId, outputMessage).parseMode(ParseMode.HTML));
            bot.execute(new SendVoice(chatId, tempFile));
            tempFile.delete();
        } catch (Exception e) {
            bot.execute(new SendMessage(chatId,
                    "<b>❌Таржума жагъанач</b>, дуьз кхьихь, месела: 1278, 354, 2, ...").parseMode(ParseMode.HTML));
        }
    }

    private String getAudioFilePath(String sound) throws URISyntaxException {
        if (Env.isRunningFromJar()) {
            // Construct the full path assuming the audio directory is next to the JAR
            File jarPath = new File(NumbersSearchResponseHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            File audioFile = new File(jarPath.getParent(), sound);
            if (!audioFile.exists()) {
                System.err.println("Audio file not found: " + audioFile);
                return null;
            }
            return audioFile.getAbsolutePath();
        } else {
            URL resource = NumbersSearchResponseHandler.class.getResource(sound);
            assert resource != null;
            return resource.toURI().getPath();
        }
    }

    private boolean isNumber(String message) {
        return message.toLowerCase().equals(message.toUpperCase());
    }
}