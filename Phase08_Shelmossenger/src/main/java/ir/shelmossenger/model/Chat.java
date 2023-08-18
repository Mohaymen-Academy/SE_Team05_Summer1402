package ir.shelmossenger.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name = "chats")
@NoArgsConstructor
@Getter
@Setter
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String link;

    private ChatType chatType;

    @Column(name = "created_at", columnDefinition = "DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
