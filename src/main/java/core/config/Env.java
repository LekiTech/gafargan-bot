package core.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {

    private Dotenv dotenv;
    private static Env envInstance;

    private Env() {
        dotenv = Dotenv.configure().ignoreIfMissing().load();
    }

    public static Env instance() {
        if (envInstance == null) {
            envInstance = new Env();
        }
        return envInstance;
    }

    public String getTelegramApiToken() {
        String apiToken = System.getenv("TELEGRAM_API_TOKEN");
        if (apiToken == null) {
            apiToken = dotenv.get("TELEGRAM_API_TOKEN");
        }
        return apiToken;
    }

    public static boolean isRunningFromJar() {
        String resourcePath = Env.class.getResource("Env.class").toString();
        return resourcePath.startsWith("jar:");
    }
}