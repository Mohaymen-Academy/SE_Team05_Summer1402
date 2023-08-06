package ir.shelmossenger.model;

import java.time.Instant;
import lombok.Data;

@Data
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
