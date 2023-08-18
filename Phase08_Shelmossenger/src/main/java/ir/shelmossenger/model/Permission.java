package ir.shelmossenger.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {
    MESSAGE("message"),
    IMAGE("image"),
    VIDEO("video"),
    VOICE("voice"),
    FILE("file"),
    ADMIN("admin");

    private final String title;
}
