package local.epul4a.fotosharing.service;

import local.epul4a.fotosharing.data.entity.User;
import local.epul4a.fotosharing.presentation.dto.UserProfileDTO;
import local.epul4a.fotosharing.presentation.dto.UserRegistrationDTO;

import java.util.List;

public interface UserService {
    void saveUser(UserRegistrationDTO userRegistrationDTO);

    User findByEmail(String email);

    List<UserProfileDTO> findAllUsers();
}
