package ir.shelmossenger.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name = "read_message")
@NoArgsConstructor
@Setter
@Getter
public class ReadMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "message_id", unique = true)
    private Message message;

    @Column(name = "read_at", nullable = false)
    private Instant readAt;
}
