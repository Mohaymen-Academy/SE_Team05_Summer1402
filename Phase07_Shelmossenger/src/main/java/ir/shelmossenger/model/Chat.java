package ir.shelmossenger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.Instant;

@AllArgsConstructor
@Getter
@Builder
public class Chat {

    private Long id;

    private String title;

    private String link;

    private ChatType chatType;

    private Instant createdAt;

    private Instant deletedAt;
}
