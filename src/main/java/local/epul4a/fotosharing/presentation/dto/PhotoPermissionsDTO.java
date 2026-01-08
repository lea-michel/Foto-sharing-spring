package local.epul4a.fotosharing.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO pour retourner les permissions d'un utilisateur sur une photo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoPermissionsDTO {
    private boolean canRead;
    private boolean canComment;
    private boolean canAdmin;
    private boolean canDelete;
    private boolean isOwner;
}
