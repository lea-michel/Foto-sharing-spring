package local.epul4a.fotosharing.persistence.repository;

import local.epul4a.fotosharing.data.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository  extends JpaRepository<Album, Long> {
    List<Album> findByOwnerId(Long ownerId);
}
