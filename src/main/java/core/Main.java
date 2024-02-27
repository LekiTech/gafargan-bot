package core;

import core.config.Env;
import core.bothandler.BotUpdates;
import core.database.DataStorage;

/**
 * The Main class is the entry point of the Telegram bot application.
 * It initializes necessary components and starts the bot updates processing.
 */
public class Main {

    /**
     * The main method of the application.
     *
     * @param args Command-line arguments (not used in this application).
     * @throws Exception if there's an error during bot initialization or execution.
     */
    public static void main(String[] args) throws Exception {
        var token = Env.instance().getTelegramApiToken();
        if (token == null) {
            System.err.println("Telegram token not found");
            return;
        }
        /* initialize singleton */
        DataStorage.instance();
        BotUpdates bot = new BotUpdates(token);
        /* Start the bot updates processing */
        bot.start();
    }
}