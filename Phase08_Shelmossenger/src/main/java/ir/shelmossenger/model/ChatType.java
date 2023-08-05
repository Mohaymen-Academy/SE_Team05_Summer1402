package ir.shelmossenger.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "chat_types")
public class ChatType {
    public static ChatType PV=new ChatType(1, "PV");
    public static ChatType GROUP=new ChatType(2, "Group");
    public static ChatType CHANNEL=new ChatType(3, "Channel");
    public ChatType(){}
    ChatType(long id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name")
    private String typeName;

}