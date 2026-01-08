package local.epul4a.fotosharing.persistence.repository;

import local.epul4a.fotosharing.data.entity.Photo;
import local.epul4a.fotosharing.data.entity.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    /* Photos publiques (non connecté) */
    Page<Photo> findByVisibility(Visibility visibility, Pageable pageable);

    /* Photos visibles pour un utilisateur connecté */
    @Query("""
        SELECT DISTINCT p FROM Photo p
        LEFT JOIN Partage pr ON pr.photo = p AND pr.user.id = :userId
        WHERE p.visibility = :publicVisibility
           OR p.owner.id = :userId
           OR pr.id IS NOT NULL
    """)
    Page<Photo> findVisiblePhotosForUser(@Param("userId") Long userId, @Param("publicVisibility") Visibility publicVisibility,
                                         Pageable pageable);

    /* Pour accès sécurisé à une photo */
    Optional<Photo> findById(Long id);

}
