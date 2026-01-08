package local.epul4a.fotosharing.service.user;

import local.epul4a.fotosharing.data.entity.User;
import local.epul4a.fotosharing.persistence.repository.UserRepository;
import local.epul4a.fotosharing.presentation.dto.UserProfileDTO;
import local.epul4a.fotosharing.presentation.dto.UserRegistrationDTO;
import local.epul4a.fotosharing.service.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public void saveUser(UserRegistrationDTO userRegistrationDTO) {

        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        User user = new User();
        user.setUsername(userRegistrationDTO.getFirstName() + " " + userRegistrationDTO.getLastName());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        userRepository.save(user); //user saved as enabled by default
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserProfileDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserProfileDTO)
                .collect(Collectors.toList());
    }
}
