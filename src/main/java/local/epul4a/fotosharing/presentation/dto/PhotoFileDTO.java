package local.epul4a.fotosharing.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO pour transférer les informations nécessaires au chargement d'une image
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoFileDTO {
    private String storageFilename;
    private String contentType;
}
