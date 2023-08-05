package ir.shelmossenger.model;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "user_name")
    private String username;

    private String password;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String bio;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}