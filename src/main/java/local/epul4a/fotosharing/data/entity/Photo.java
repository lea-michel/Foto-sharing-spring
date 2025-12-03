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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "photos")
public class Photo {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "original_filename")
    private String originalFilename;
    @Column(name = "storage_filename", nullable = false, unique = true)
    private String storageFilename;
    @Column(name = "content_type", nullable = false, length = 50)
    private String contentType;
    @Enumerated(EnumType.STRING)
    private Visibility visibility = Visibility.PRIVATE;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "owner_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_photo_owner")
    )
    @OnDelete(action = OnDeleteAction.CASCADE) // Active le ON DELETE CASCADE
    private User owner;
    @Column(name = "created_at",
            nullable = false,
            updatable = false,
            insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

}
