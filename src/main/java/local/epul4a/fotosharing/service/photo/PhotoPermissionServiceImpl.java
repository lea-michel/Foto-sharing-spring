package local.epul4a.fotosharing.service.photo;

import local.epul4a.fotosharing.data.entity.PermissionLevel;
import local.epul4a.fotosharing.data.entity.Photo;
import local.epul4a.fotosharing.data.entity.User;
import local.epul4a.fotosharing.data.entity.Visibility;
import local.epul4a.fotosharing.persistence.repository.PartageRepository;
import local.epul4a.fotosharing.persistence.repository.PhotoRepository;
import org.springframework.stereotype.Service;

@Service
public class PhotoPermissionServiceImpl implements PhotoPermissionService {

    private final PhotoRepository photoRepository;
    private final PartageRepository partageRepository;

    public PhotoPermissionServiceImpl(PhotoRepository photoRepository,
                                  PartageRepository partageRepository) {
        this.photoRepository = photoRepository;
        this.partageRepository = partageRepository;
    }


    @Override
    public boolean canRead(Photo photo, User user) {

        // PUBLIC → tout le monde
        if (photo.getVisibility() == Visibility.PUBLIC) {
            return true;
        }

        // Propriétaire
        if (photo.getOwner().equals(user)) {
            return true;
        }

        // Partage explicite
        return user != null &&
                partageRepository.existsByPhotoIdAndUserId(photo.getId(), user.getId());
    }

    @Override
    public boolean canComment(Photo photo, User user) {
        if (user == null) return false;

        if (photo.getOwner().getId().equals(user.getId())) return true;

        return partageRepository.findByPhotoIdAndUserId(photo.getId(), user.getId())
                .map(p ->
                        p.getPermissionLevel() == PermissionLevel.COMMENT
                        || p.getPermissionLevel() == PermissionLevel.ADMIN)
                .orElse(false);
    }

    @Override
    public boolean canAdmin(Photo photo, User user) {
        if (user == null) return false;

        if (photo.getOwner().equals(user)) return true;

        return partageRepository.findByPhotoIdAndUserId(photo.getId(), user.getId())
                .map(p -> p.getPermissionLevel() == PermissionLevel.ADMIN)
                .orElse(false);
    }

    @Override
    public boolean canDelete(Photo photo, User user) {
        return canAdmin(photo, user);
    }
}
