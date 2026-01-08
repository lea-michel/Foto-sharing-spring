package local.epul4a.fotosharing.service.mapper;

import local.epul4a.fotosharing.data.entity.Partage;
import local.epul4a.fotosharing.data.entity.Photo;
import local.epul4a.fotosharing.data.entity.User;
import local.epul4a.fotosharing.data.entity.Visibility;
import local.epul4a.fotosharing.data.model.PhotoStorageInformation;
import local.epul4a.fotosharing.presentation.dto.PhotoDTO;
import local.epul4a.fotosharing.presentation.dto.PhotoDetailsDTO;
import local.epul4a.fotosharing.presentation.dto.PhotoUploadDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhotoMapper {
    private final UserMapper userMapper;
    private final SharedUserMapper sharedUserMapper;

    public PhotoMapper(UserMapper userMapper, SharedUserMapper sharedUserMapper) {
        this.userMapper = userMapper;
        this.sharedUserMapper = sharedUserMapper;
    }

    public PhotoDTO toPhotoDTO(Photo photo) {
        return new PhotoDTO(
                photo.getId(),
                photo.getTitle(),
                photo.getDescription(),
                "/thumb/" + photo.getId(),
                photo.getVisibility(),
                photo.getCreatedAt().toString(),
                "/photo/" + photo.getId()
        );
    }

    public PhotoDetailsDTO toPhotoDetailsDTO(Photo photo, List<Partage> partages) {
        return new PhotoDetailsDTO(
                photo.getId(),
                photo.getTitle(),
                photo.getDescription(),
                photo.getOriginalFilename(),
                photo.getStorageFilename(),
                photo.getVisibility().name(),
                userMapper.toUserProfileDTO(photo.getOwner()),
                photo.getCreatedAt().toString(),
                sharedUserMapper.toDtoList(partages)

        );
    }

    public Photo toEntity(
            PhotoUploadDTO dto,
            PhotoStorageInformation storage,
            User owner) {

        Photo photo = new Photo();
        photo.setTitle(dto.getTitle());
        photo.setDescription(dto.getDescription());
        photo.setVisibility(Visibility.valueOf(dto.getVisibility()));

        photo.setOriginalFilename(storage.getOriginalFilename());
        photo.setStorageFilename(storage.getStorageFilename());
        photo.setContentType(storage.getContentType());

        photo.setOwner(owner);

        return photo;
    }
}
