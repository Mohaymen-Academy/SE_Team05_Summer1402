package ir.shelmossenger.model;

public enum ChatType {
    PV(1, "PV"),
    GROUP(2, "Group"),
    CHANNEL(3, "Channel");

    private final int id;
    private final String typeName;

    ChatType(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }
}