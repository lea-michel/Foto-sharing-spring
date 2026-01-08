package local.epul4a.fotosharing.presentation.controller;

import local.epul4a.fotosharing.data.entity.Photo;
import local.epul4a.fotosharing.data.entity.User;
import local.epul4a.fotosharing.presentation.dto.PhotoFileDTO;
import local.epul4a.fotosharing.service.exception.AccessDeniedException;
import local.epul4a.fotosharing.service.photo.PhotoPermissionService;
import local.epul4a.fotosharing.service.photo.PhotoService;
import local.epul4a.fotosharing.service.photo.PhotoStorageService;
import local.epul4a.fotosharing.service.security.UserPrincipal;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImageController {

    private final PhotoService photoService;

    public ImageController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/img/photos/{photoId}")
    public ResponseEntity<byte[]> getPhoto(
            @PathVariable Long photoId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Long userId = principal != null ? principal.getUser().getId() : null;

        PhotoFileDTO fileInfo = photoService.getPhotoFileInfo(photoId, userId);

        // ✅ Service charge le fichier
        byte[] data = photoService.loadPhotoFile(fileInfo.getStorageFilename());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, fileInfo.getContentType())
                .body(data);
    }

    @GetMapping("/thumb/{photoId}")
    public ResponseEntity<byte[]> getThumbnail(
            @PathVariable Long photoId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        Long userId = principal != null ? principal.getUser().getId() : null;

        // Service vérifie les permissions et retourne un DTO
        PhotoFileDTO fileInfo = photoService.getPhotoFileInfo(photoId, userId);

        // Service charge la miniature
        byte[] data = photoService.loadPhotoThumbnail(fileInfo.getStorageFilename());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(data);
    }
}
