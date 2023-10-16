package core;

public class Main {

    public static void main(String[] args) throws Exception {
        var token = Env.instance().getTelegramApiToken();
        if (token == null) {
            System.err.println("Telegram token not found");
            return;
        }
        /* initialize singleton */
        DataStorage.instance();
        BotUpdates bot = new BotUpdates(token);
        bot.start();
    }
}