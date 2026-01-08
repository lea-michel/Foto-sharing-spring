package local.epul4a.fotosharing.presentation.dto;

import local.epul4a.fotosharing.data.entity.Visibility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//affichage liste
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDTO {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Visibility visibility;
    private String createdAt;
    private String imageUrl;
}
