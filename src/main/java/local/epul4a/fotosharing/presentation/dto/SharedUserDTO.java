package local.epul4a.fotosharing.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//pour voir avec qui la photo est partagée
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SharedUserDTO {
    private Long userId;
    private String username;
    private String permission;
}

