package ir.shelmossenger.model;

import java.time.Instant;

import lombok.Data;

@Data
public class Chat {

    private Long id;
    private String title;
    private String link;
    private ChatType chatType;
    private Instant createdAt;
    private Instant deletedAt;

}