package local.epul4a.fotosharing.persistence.repository;

import local.epul4a.fotosharing.data.entity.Partage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartageRepository extends JpaRepository<Partage, Long> {

    List<Partage> findByPhotoId(Long photoId);

    boolean existsByPhotoIdAndUserId(Long photoId, Long userId);

    Optional<Partage> findByPhotoIdAndUserId(Long photoId, Long userId);

}

