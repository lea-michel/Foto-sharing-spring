package local.epul4a.fotosharing.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShareRequestDTO {
    private Long userId;
    private Long photoId;
    private String permission; // READ, COMMENT, ADMIN
}
