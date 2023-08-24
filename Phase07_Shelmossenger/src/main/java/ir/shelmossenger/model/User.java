package ir.shelmossenger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.Instant;

@AllArgsConstructor
@Getter
@Builder
public class User {

    private Long id;

    private String fullName;

    private String userName;

    private String password;

    private String email;

    private String phoneNumber;

    private String bio;

    private Instant deletedAt;
}
