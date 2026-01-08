package local.epul4a.fotosharing.service.user;

import local.epul4a.fotosharing.data.entity.User;
import local.epul4a.fotosharing.persistence.repository.UserRepository;
import local.epul4a.fotosharing.presentation.dto.UserRegistrationDTO;
import local.epul4a.fotosharing.presentation.dto.UserSummaryDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public List<UserSummaryDTO> getAllUsersSummary() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToUserSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        user.setEnabled(!user.getEnabled());
        userRepository.save(user);
    }

    @Override
    public UserSummaryDTO getUserSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        return convertToUserSummaryDTO(user);
    }

    // Méthode privée pour convertir User -> UserSummaryDTO
    private UserSummaryDTO convertToUserSummaryDTO(User user) {
        UserSummaryDTO dto = new UserSummaryDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setEnabled(user.getEnabled());
        dto.setRole(user.getRole().name());
        return dto;
    }
}
