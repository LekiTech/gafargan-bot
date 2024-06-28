package core.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendVoice;
import core.config.Env;
import core.searchers.NumeralSearchResponseHandler;
import io.lekitech.LezgiNumbers;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;

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
import java.util.ArrayList;
import java.util.List;

import static io.lekitech.LezgiNumbers.numToLezgiTTS;

@AllArgsConstructor
public class ResponseFromNumeralAudioButtonCommandProcessor implements ChatCommandProcessor {

    private final Message message;
    private final TelegramBot bot;
    private final String numeral;

    @Override
    public void execute() throws NotFoundException {
        final var chatId = message.chat().id();
        File audioFile = getAudio(numeral);
        bot.execute(new SendVoice(chatId, audioFile));
    }

    private File getAudio(String userMessage) {
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
            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAudioFilePath(String sound) throws URISyntaxException {
        if (Env.isRunningFromJar()) {
            // Construct the full path assuming the audio directory is next to the JAR
            File jarPath = new File(NumeralSearchResponseHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            File audioFile = new File(jarPath.getParent(), sound);
            if (!audioFile.exists()) {
                System.err.println("Audio file not found: " + audioFile);
                return null;
            }
            return audioFile.getAbsolutePath();
        } else {
            URL resource = NumeralSearchResponseHandler.class.getResource(sound);
            assert resource != null;
            return resource.toURI().getPath();
        }
    }
}
