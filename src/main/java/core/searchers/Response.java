package core.searchers;

import java.util.List;

public record Response(String messageText, List<String> suggestions, String buttonKey) {

    public Response(String messageText) {
        this(messageText, null, null);
    }

    public Response(String messageText, List<String> suggestions) {
        this(messageText, suggestions, null);
    }

    public Response(String messageText, String buttonKey) {
        this(messageText, null, buttonKey);
    }
}