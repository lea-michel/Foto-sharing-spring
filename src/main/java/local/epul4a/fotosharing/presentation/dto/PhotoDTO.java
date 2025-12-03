package local.epul4a.fotosharing.presentation.dto;

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
    private String url;           // URL publique (via Nginx)
    private String ownerUsername;
    private String visibility;
    private String createdAt;
}
