package local.epul4a.fotosharing.persistence.repository;

import local.epul4a.fotosharing.data.entity.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    List<Commentaire> findByPhotoIdOrderByCreatedAtDesc(Long photoId);
}
