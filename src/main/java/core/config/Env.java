package core.config;

import io.github.cdimascio.dotenv.Dotenv;

public final class Env {

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

    public String getSecretKey() {
        String secretKey = System.getenv("SECRET_KEY");
        if (secretKey == null) {
            secretKey = dotenv.get("SECRET_KEY");
        }
        return secretKey;
    }

    public Long getSecretId() {
        String secretId = System.getenv("SECRET_ID");
        if (secretId == null) {
            secretId = dotenv.get("SECRET_ID");
        }
        return Long.valueOf(secretId);
    }

    public static boolean isRunningFromJar() {
        String resourcePath = Env.class.getResource("Env.class").toString();
        return resourcePath.startsWith("jar:");
    }
}