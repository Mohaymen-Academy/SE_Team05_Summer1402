package ir.shelmossenger.model;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission {
    public static Permission MESSAGE = new Permission(1, "message");
    public static Permission IMAGE = new Permission(2, "image");
    public static Permission VIDEO = new Permission(3, "video");
    public static Permission VOICE = new Permission(4, "voice");
    public static Permission FILE = new Permission(5, "file");
    public static Permission ADMIN = new Permission(6, "admin");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    public Permission() {
    }

    Permission(long id, String title) {
        this.id = id;
        this.title = title;
    }

}