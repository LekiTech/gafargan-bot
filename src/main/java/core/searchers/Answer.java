package core.searchers;

import java.util.List;

public record Answer(String messageText, List<String> exampleButton) {
    public Answer(String messageText) {
        this(messageText, null);
    }
}