package ir.shelmossenger.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "profile_images")
public class ProfileImage {

    @Id
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "created_at")
    private Instant createdAt;

    // getters and setters
}