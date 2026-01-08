package local.epul4a.fotosharing.service.mapper;

import local.epul4a.fotosharing.data.entity.User;
import local.epul4a.fotosharing.presentation.dto.UserProfileDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserProfileDTO toUserProfileDTO(User user) {

        if (user == null) {
            return null;
        }

        return new UserProfileDTO(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
