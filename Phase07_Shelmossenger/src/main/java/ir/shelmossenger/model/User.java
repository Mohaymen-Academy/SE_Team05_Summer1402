package ir.shelmossenger.model;

import java.time.Instant;

import lombok.Data;

@Data
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
