package core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public TelegramBot telegramBot() {
        var token = Env.instance().getTelegramApiToken();
        if (token == null) {
            System.err.println("Telegram token not found");
            return null;
        }
        return new TelegramBot(token);
    }
}

