package ir.shelmossenger.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum MessageType {
    MESSAGE("message"),
    IMAGE("image"),
    VIDEO("video"),
    VOICE("voice"),
    FILE("file"),
    NOTIFICATION("notification");

    private final String typeName;

    public static MessageType getByTitle(String title) {
        return Arrays.stream(values()).filter(m -> m.typeName.equals(title)).findFirst().orElse(MESSAGE);
    }
}
