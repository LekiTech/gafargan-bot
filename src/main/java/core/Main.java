package core;

import core.config.Env;
import core.bothandler.BotUpdates;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(Main.class);
        ApplicationContext context = application.run(args);
        var token = Env.instance().getTelegramApiToken();
        if (token == null) {
            System.err.println("Telegram token not found");
            return;
        }
        BotUpdates bot = new BotUpdates(token, context);
        /* Start the bot updates processing */
        bot.start();
//        SpringApplication.run(Main.class, args);

//        ApplicationContext context = SpringApplication.run(Main.class, args);
//        UserService userService = context.getBean(UserService.class);
//        userService.saveUser(new User(1998L, new Timestamp(System.currentTimeMillis())));


//        ApplicationContext context = SpringApplication.run(Main.class, args);
//        KamranService kamran = context.getBean(KamranService.class);
//        kamran.save(new Kamran(UUID.randomUUID(), "Sample Name", new Timestamp(System.currentTimeMillis())));

//        SpringApplication application = new SpringApplication(Main.class);
//        ApplicationContext context = application.run(args);
//        KamranService kamran = context.getBean(KamranService.class);
//        kamran.save(new Kamran(UUID.randomUUID(), "3kamrann", new Timestamp(System.currentTimeMillis())));
    }
}