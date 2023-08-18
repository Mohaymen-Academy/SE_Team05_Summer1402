package ir.shelmossenger.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_chat_permission")
public class UserChatPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_chat_id")
    private UserChat userChat;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "permission_id")
    private Permission permission;
}