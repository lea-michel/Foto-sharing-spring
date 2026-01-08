package local.epul4a.fotosharing.service.mapper;

import local.epul4a.fotosharing.data.entity.Partage;
import local.epul4a.fotosharing.data.entity.User;
import local.epul4a.fotosharing.presentation.dto.SharedUserDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SharedUserMapper {

    public SharedUserDTO toDto(Partage partage) {

        return new SharedUserDTO(
                partage.getUser().getId(),
                partage.getUser().getUsername(),
                partage.getPermissionLevel().name()
        );
    }

    public List<SharedUserDTO> toDtoList(List<Partage> partages) {
        return partages.stream()
                .map(this::toDto)
                .toList();
    }
}

