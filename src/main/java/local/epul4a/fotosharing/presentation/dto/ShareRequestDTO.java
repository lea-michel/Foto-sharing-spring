package local.epul4a.fotosharing.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShareRequestDTO {
    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private Long userId;

    private Long photoId;

    @NotNull(message = "Le niveau de permission est obligatoire")
    private String permission; // READ, COMMENT, ADMIN
}
