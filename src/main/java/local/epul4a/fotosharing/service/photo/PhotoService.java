package local.epul4a.fotosharing.service.photo;

import local.epul4a.fotosharing.presentation.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PhotoService {

    //listing
    Page<PhotoDTO> getPublicPhotos(Pageable pageable);

    Page<PhotoDTO> getVisiblePhotosForUser(Long userId, Pageable pageable);

    //CRUD
    void upload(PhotoUploadDTO dto, Long userId);

    PhotoDetailsDTO getPhotoDetails(Long id, Long userId);

    void updatePhoto(Long photoId, PhotoUpdateDTO dto, Long currentUserId);

    void deletePhoto(Long photoId, Long userId);


    //Permissions
    PhotoPermissionsDTO getUserPermissions(Long photoId, Long userId);

    //Partage
    void sharePhoto(ShareRequestDTO shareRequest, Long ownerId);

    void unsharePhoto(Long photoId, Long targetUserId, Long currentUserId);

    // Commentaires
    void addComment(CommentCreateDTO commentDTO, Long authorId);

    List<CommentDTO> getComments(Long photoId);

    PhotoFileDTO getPhotoFileInfo(Long photoId, Long currentUserId);
    byte[] loadPhotoFile(String storageFilename);
    byte[] loadPhotoThumbnail(String storageFilename);

}
