package ir.shelmossenger.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    public static MessageType getById(int id) {
        return values()[id];
    }
}
