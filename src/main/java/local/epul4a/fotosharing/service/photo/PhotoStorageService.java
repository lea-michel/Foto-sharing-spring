package local.epul4a.fotosharing.service.photo;

import local.epul4a.fotosharing.data.model.PhotoStorageInformation;
import local.epul4a.fotosharing.presentation.dto.PhotoUploadDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorageService {
    void init();

    PhotoStorageInformation save(PhotoUploadDTO dto, MultipartFile file);

    void delete(String filename);


    byte[] loadThumbnail(String storageFilename);

    byte[] loadPhoto(String storageFilename);

    String getFullImageUrl(String storageFilename);
}
