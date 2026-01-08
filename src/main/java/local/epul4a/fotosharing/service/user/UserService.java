package local.epul4a.fotosharing.service.user;

import local.epul4a.fotosharing.data.entity.User;
import local.epul4a.fotosharing.presentation.dto.UserProfileDTO;
import local.epul4a.fotosharing.presentation.dto.UserRegistrationDTO;
import local.epul4a.fotosharing.presentation.dto.UserSummaryDTO;

import java.util.List;

public interface UserService {
    void saveUser(UserRegistrationDTO userRegistrationDTO);

    User findByEmail(String email);

    List<UserSummaryDTO> getAllUsersSummary();
    void toggleUserStatus(Long userId);
    UserSummaryDTO getUserSummary(Long userId);
}
