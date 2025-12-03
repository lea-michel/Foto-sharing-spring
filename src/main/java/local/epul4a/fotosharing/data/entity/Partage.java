package local.epul4a.fotosharing.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "partage",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_photo_user", columnNames = {"photo_id", "user_id"})
        },
        indexes = {
                @Index(name = "idx_partage_user", columnList = "user_id")
        }
)
public class Partage {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_partage_user")
    )
    @OnDelete(action = OnDeleteAction.CASCADE) // Active le ON DELETE CASCADE
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "photo_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_partage_photo")
    )
    @OnDelete(action = OnDeleteAction.CASCADE) // Active le ON DELETE CASCADE
    private Photo photo;

    @Enumerated(EnumType.STRING)
    private PermissionLevel permissionLevel = PermissionLevel.READ;

    @Column(name = "created_at",
            nullable = false,
            updatable = false,
            insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

}
