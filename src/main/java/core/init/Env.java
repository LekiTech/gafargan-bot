package core.init;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {

    private Dotenv dotenv;
    private static Env envInstance;

    private Env() {
        dotenv = Dotenv.configure().load();
    }

    public static Env instance() {
        if (envInstance == null) {
            envInstance = new Env();
        }
        return envInstance;
    }

    public String getTelegramApiToken() {
        return dotenv.get("TELEGRAM_API_TOKEN");
    }
}