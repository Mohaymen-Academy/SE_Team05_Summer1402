package ir.shelmossenger.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Arrays;

@Data
@Entity
@Table(name = "message_types")
public class MessageType {
    public static MessageType MESSAGE=new MessageType(1, "message");
    public static MessageType IMAGE=new MessageType(2, "image");
    public static MessageType VIDEO=new MessageType(3, "video");
    public static MessageType VOICE=new MessageType(4, "voice");
    public static MessageType FILE=new MessageType(5, "file");
    public static MessageType NOTIFICATION=new MessageType(6, "notification");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name")
    private String typeName;
    public MessageType(){}

    MessageType(long id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public static MessageType getById(int id) {
        return values[id];
    }
    public static MessageType getByTitle(String title) {
        return Arrays.stream(values).filter(m->m.typeName.equals(title)).findFirst().get();
    }
    private static MessageType[] values=new MessageType[]{
            MESSAGE,
            IMAGE,
            VIDEO,
            VOICE,
            FILE,
            NOTIFICATION,
    };
}