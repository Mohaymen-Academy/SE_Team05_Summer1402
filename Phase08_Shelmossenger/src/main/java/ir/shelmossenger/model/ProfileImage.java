package ir.shelmossenger.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Entity
@Table(name = "profile_images")
@NoArgsConstructor
@Getter
@Setter
public class ProfileImage {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "profile_image_url", nullable = false)
    private String profileImageUrl;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;
}
