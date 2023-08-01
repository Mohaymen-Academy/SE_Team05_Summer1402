package ir.shelmossenger.model;

public enum Permission {
    MESSAGE(1, "message"),
    IMAGE(2, "image"),
    VIDEO(3, "video"),
    VOICE(4, "voice"),
    FILE(5, "file"),
    ADMIN(6, "admin");

    private final int id;
    private final String title;

    Permission(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}