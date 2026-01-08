package local.epul4a.fotosharing.service.photo;

import local.epul4a.fotosharing.data.entity.Photo;
import local.epul4a.fotosharing.data.entity.User;

public interface PhotoPermissionService {
    boolean canRead(Photo photo, User user);
    boolean canComment(Photo photo, User user);
    boolean canAdmin(Photo photo, User user);
    boolean canDelete(Photo photo, User user);
}
