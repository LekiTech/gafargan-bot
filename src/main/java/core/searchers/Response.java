package core.searchers;

import java.util.List;

public record Response(String messageText, List<String> exampleButton) {
    public Response(String messageText) {
        this(messageText, null);
    }
}