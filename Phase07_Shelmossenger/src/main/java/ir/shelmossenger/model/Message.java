package ir.shelmossenger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.Instant;

@AllArgsConstructor
@Getter
@Builder
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
