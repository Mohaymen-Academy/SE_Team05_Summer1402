package ir.shelmossenger.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_chat_permission")
@NoArgsConstructor
@Setter
@Getter
public class UserChatPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_chat_id", nullable = false)
    private UserChat userChat;

    @Enumerated(EnumType.ORDINAL)
    private Permission permission;
}
