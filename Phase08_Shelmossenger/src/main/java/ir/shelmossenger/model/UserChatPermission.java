package ir.shelmossenger.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_chat_permission", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_chat_id", "permission"})})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
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
