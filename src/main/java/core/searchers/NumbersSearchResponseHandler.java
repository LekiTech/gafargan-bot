package core.searchers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendVoice;
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
    private final Map<String, String> map = new HashMap<>();

    {
        map.put("/audio/_.wav", "/audio/0.wav");
        map.put("/audio/агъзур.wav", "/audio/agzur.wav");
        map.put("/audio/ни.wav", "/audio/ni.wav");
        map.put("/audio/вад.wav", "/audio/vad.wav");
        map.put("/audio/виш.wav", "/audio/vish.wav");
        map.put("/audio/ирид.wav", "/audio/irid.wav");
        map.put("/audio/нул.wav", "/audio/nol.wav");
        map.put("/audio/сад.wav", "/audio/sad.wav");
        map.put("/audio/ругуд.wav", "/audio/rugud.wav");
        map.put("/audio/минус.wav", "/audio/minus.wav");
        map.put("/audio/пуд.wav", "/audio/pud.wav");
        map.put("/audio/миллион.wav", "/audio/million.wav");
        map.put("/audio/миллиард.wav", "/audio/milliard.wav");
        map.put("/audio/къад.wav", "/audio/kkad.wav");
        map.put("/audio/къанни.wav", "/audio/kkanni.wav");
        map.put("/audio/рид.wav", "/audio/rid.wav");
        map.put("/audio/цIе.wav", "/audio/tsIe.wav");
        map.put("/audio/триллион.wav", "/audio/trillion.wav");
        map.put("/audio/муьжуьд.wav", "/audio/mujud.wav");
        map.put("/audio/кIуьд.wav", "/audio/kIud.wav");
        map.put("/audio/цIи.wav", "/audio/tsIi.wav");
        map.put("/audio/цIу.wav", "/audio/tsIy.wav");
        map.put("/audio/цIуд.wav", "/audio/tsIyd.wav");
        map.put("/audio/яхцIур.wav", "/audio/yaxtsIyr.wav");
        map.put("/audio/квадриллион.wav", "/audio/kvadrillion.wav");
        map.put("/audio/квинтиллион.wav", "/audio/kvintillion.wav");
        map.put("/audio/септиллион.wav", "/audio/septillion.wav");
        map.put("/audio/секстиллион.wav", "/audio/sekstillion.wav");
        map.put("/audio/нониллион.wav", "/audio/nonillion.wav");
        map.put("/audio/октиллион.wav", "/audio/oktillion.wav");
        map.put("/audio/кьуд.wav", "/audio/kbyd.wav");
        map.put("/audio/кьве.wav", "/audio/kbve.wav");
        map.put("/audio/кьвед.wav", "/audio/kbved.wav");
    }

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
                sound = map.get(sound);
                URL resource = NumbersSearchResponseHandler.class.getResource(sound);
                assert resource != null;
                try {
                    String path = resource.toURI().getPath();
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

    private boolean isNumber(String message) {
        return message.toLowerCase().equals(message.toUpperCase());
    }
}