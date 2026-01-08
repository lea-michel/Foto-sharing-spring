package local.epul4a.fotosharing.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoUpdateDTO {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private String visibility; // PUBLIC or PRIVATE
}
