package ir.shelmossenger.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@NoArgsConstructor
@Setter
@Getter
public class Chat {

    private Long id;

    private String title;

    private String link;

    private ChatType chatType;

    private Instant createdAt;

    private Instant deletedAt;
}
