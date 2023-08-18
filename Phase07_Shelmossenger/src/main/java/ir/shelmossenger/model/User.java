package ir.shelmossenger.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@NoArgsConstructor
@Setter
@Getter
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
