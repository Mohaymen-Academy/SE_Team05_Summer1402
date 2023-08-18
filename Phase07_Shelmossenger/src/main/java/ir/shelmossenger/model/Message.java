package ir.shelmossenger.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@NoArgsConstructor
@Setter
@Getter
public class Message {

    private Long id;

    private String data;

    private MessageType messageType;

    private Instant sentAt;

    private Instant editedAt;

    private Instant deletedAt;

    private Long senderId;

    private Long chatId;

    private Long replyId;
}
