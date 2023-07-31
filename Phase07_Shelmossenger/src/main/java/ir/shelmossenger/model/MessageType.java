package ir.shelmossenger.model;

public enum MessageType {
    MESSAGE(1, "message"),
    IMAGE(2, "image"),
    VIDEO(3, "video"),
    VOICE(4, "voice"),
    FILE(5, "file"),
    NOTIFICATION(6, "notification");

    private final int id;
    private final String typeName;

    MessageType(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public static MessageType getById(int id) {
        return values()[id];
    }
}