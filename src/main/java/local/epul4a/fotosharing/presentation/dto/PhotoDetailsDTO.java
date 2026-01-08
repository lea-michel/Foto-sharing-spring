package local.epul4a.fotosharing.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//detail, admin, security
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDetailsDTO {
    private Long id;
    private String title;
    private String description;
    private String originalFilename;
    private String storageFilename;
    private String visibility;
    private UserProfileDTO owner;
    private String createdAt;
    List<SharedUserDTO> sharedUsers;
}
