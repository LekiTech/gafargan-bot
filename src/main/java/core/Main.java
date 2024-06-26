package core;

import core.config.Env;
import core.bothandler.BotUpdates;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws Exception {
        // NOTE: not the best way to do this, but it works for current Spring usage
        // NOTE: next time either use appropriate Spring Boot or use Hibernate directly for db connection if API is not needed
        // Create a map to hold your database properties
        Map<String, Object> dbProperties = Env.instance().getDatabaseProperties();
        // Use SpringApplicationBuilder to set the properties
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
        builder.properties(dbProperties);

        // Build and run the application
        ApplicationContext context = builder.run(args);

//        var token = Env.instance().getTelegramApiToken();
//        if (token == null) {
//            System.err.println("Telegram token not found");
//            return;
//        }
        BotUpdates bot = context.getBean(BotUpdates.class);
        /* Start the bot updates processing */
        bot.start();
    }
}