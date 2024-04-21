package core.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.net.URL;

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

    public static boolean isRunningFromJar() {
        String resourcePath = Env.class.getResource("Env.class").toString();
        return resourcePath.startsWith("jar:");
    }
}