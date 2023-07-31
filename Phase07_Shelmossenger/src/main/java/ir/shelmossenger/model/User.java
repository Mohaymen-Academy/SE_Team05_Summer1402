package ir.shelmossenger.model;

import lombok.Data;

@Data
public class User {

    private Long id;

    private String fullName;

    private String userName;

    private String email;

    private String phoneNumber;

    private String bio;

    private Instant deletedAt;

}
