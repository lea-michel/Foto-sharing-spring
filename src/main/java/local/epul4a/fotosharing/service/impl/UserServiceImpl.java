package local.epul4a.fotosharing.service.impl;

import local.epul4a.fotosharing.data.entity.User;
import local.epul4a.fotosharing.presentation.dto.UserProfileDTO;
import local.epul4a.fotosharing.presentation.dto.UserRegistrationDTO;
import local.epul4a.fotosharing.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public void saveUser(UserRegistrationDTO userRegistrationDTO) {

    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public List<UserProfileDTO> findAllUsers() {
        return List.of();
    }
}
