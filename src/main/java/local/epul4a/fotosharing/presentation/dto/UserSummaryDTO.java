package local.epul4a.fotosharing.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//pour lister les utilisateurs dans admin
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDTO {
    private Long id;

    private String email;
    private String username;
    private Boolean enabled;
    private String role;
}
