package local.epul4a.fotosharing.service.photo;

import local.epul4a.fotosharing.data.entity.*;
import local.epul4a.fotosharing.data.model.PhotoStorageInformation;
import local.epul4a.fotosharing.persistence.repository.CommentaireRepository;
import local.epul4a.fotosharing.persistence.repository.PartageRepository;
import local.epul4a.fotosharing.persistence.repository.PhotoRepository;
import local.epul4a.fotosharing.persistence.repository.UserRepository;
import local.epul4a.fotosharing.presentation.dto.*;
import local.epul4a.fotosharing.service.exception.AccessDeniedException;
import local.epul4a.fotosharing.service.exception.PhotoNotFoundException;
import local.epul4a.fotosharing.service.mapper.PhotoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final PhotoPermissionService photoPermissionService;
    private final PhotoStorageService photoStorageService;
    private final PhotoMapper photoMapper;
    private final PartageRepository partageRepository;
    private final UserRepository userRepository;
    private final CommentaireRepository commentaireRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository, PhotoPermissionService photoPermissionService, PhotoStorageService photoStorageService, PhotoMapper photoMapper, PartageRepository partageRepository, UserRepository userRepository, CommentaireRepository commentaireRepository) {
        this.photoRepository = photoRepository;
        this.photoPermissionService = photoPermissionService;
        this.photoStorageService = photoStorageService;
        this.photoMapper = photoMapper;
        this.partageRepository = partageRepository;
        this.userRepository = userRepository;
        this.commentaireRepository = commentaireRepository;
    }

    @Override
    public Page<PhotoDTO> getPublicPhotos(Pageable pageable) {
        return photoRepository.findByVisibility(Visibility.PUBLIC, pageable)
                .map(photoMapper::toPhotoDTO);
    }

    @Override
    public Page<PhotoDTO> getVisiblePhotosForUser(Long userId, Pageable pageable) {
        return photoRepository.findVisiblePhotosForUser(userId, Visibility.PUBLIC, pageable)
                .map(photoMapper::toPhotoDTO);
    }

    @Override
    public PhotoPermissionsDTO getUserPermissions(Long photoId, Long userId) {
        Photo photo = getPhotoEntityById(photoId);
        User user = userId != null ? getUserById(userId) : null;

        PhotoPermissionsDTO permissions = new PhotoPermissionsDTO();

        if (user != null) {
            permissions.setCanRead(photoPermissionService.canRead(photo, user));
            permissions.setCanComment(photoPermissionService.canComment(photo, user));
            permissions.setCanAdmin(photoPermissionService.canAdmin(photo, user));
            permissions.setCanDelete(photoPermissionService.canDelete(photo, user));
            permissions.setOwner(photo.getOwner().getId().equals(user.getId()));
        } else {
            // Utilisateur non connecté
            permissions.setCanRead(photo.getVisibility() == Visibility.PUBLIC);
            permissions.setCanComment(false);
            permissions.setCanAdmin(false);
            permissions.setCanDelete(false);
            permissions.setOwner(false);
        }

        return permissions;
    }

    @Override
    public void sharePhoto(ShareRequestDTO shareRequest, Long ownerId) {
        Photo photo = getPhotoEntityById(shareRequest.getPhotoId());
        User owner = getUserById(ownerId);

        // Vérification : seul le propriétaire peut partager (logique métier)
        if (!photo.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("Seul le propriétaire peut partager cette photo");
        }

        User targetUser = getUserById(shareRequest.getUserId());

        // Vérifier si un partage existe déjà
        if (partageRepository.existsByPhotoIdAndUserId(photo.getId(), targetUser.getId())) {
            throw new IllegalArgumentException("Cette photo est déjà partagée avec cet utilisateur");
        }

        Partage partage = new Partage();
        partage.setPhoto(photo);
        partage.setUser(targetUser);
        partage.setPermissionLevel(PermissionLevel.valueOf(shareRequest.getPermission()));

        partageRepository.save(partage);
    }

    @Override
    public void unsharePhoto(Long photoId, Long targetUserId, Long currentUserId) {
        Photo photo = getPhotoEntityById(photoId);
        User currentUser = getUserById(currentUserId);

        //seul le propriétaire peut retirer un partage
        if (!photo.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Seul le propriétaire peut retirer un partage");
        }

        Partage partage = partageRepository.findByPhotoIdAndUserId(photoId, targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("Partage introuvable"));

        partageRepository.delete(partage);
    }

    @Override
    public void addComment(CommentCreateDTO commentDTO, Long authorId) {
        Photo photo = getPhotoEntityById(commentDTO.getPhotoId());
        User author = getUserById(authorId);

        // Vérification des droits de commentaire (logique métier)
        if (!photoPermissionService.canComment(photo, author)) {
            throw new AccessDeniedException("Vous n'avez pas le droit de commenter cette photo");
        }

        Commentaire commentaire = new Commentaire();
        commentaire.setPhoto(photo);
        commentaire.setAuthor(author);
        commentaire.setText(commentDTO.getContent());

        commentaireRepository.save(commentaire);
    }

    @Override
    public List<CommentDTO> getComments(Long photoId) {
        List<Commentaire> commentaires = commentaireRepository.findByPhotoIdOrderByCreatedAtDesc(photoId);

        return commentaires.stream()
                .map(c -> new CommentDTO(
                        c.getId(),
                        c.getText(),
                        c.getAuthor().getUsername(),
                        c.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());
    }


    @Override
    public void deletePhoto(Long photoId, Long currentUserId) {
        Photo photo = getPhotoEntityById(photoId);
        User currentUser = getUserById(currentUserId);

        if (!photoPermissionService.canDelete(photo, currentUser)) {
            throw new AccessDeniedException("Vous n’avez pas le droit de supprimer cette photo");
        }

        // Supprimer les fichiers physiques
        photoStorageService.delete(photo.getStorageFilename());
        //Supprimer l'entité
        photoRepository.delete(photo);

    }

    @Override
    public void upload(PhotoUploadDTO dto, Long userId) {

        PhotoStorageInformation storageInfo =
                photoStorageService.save(dto, dto.getFile());

        User owner = getUserById(userId);

        Photo photo = photoMapper.toEntity(dto, storageInfo, owner);

        photoRepository.save(photo);
    }


    //page detail / admin global
    @Override
    public PhotoDetailsDTO getPhotoDetails(Long photoId, Long currentUserId) {

        Photo photo = getPhotoEntityById(photoId);

        User currentUser = currentUserId != null ? getUserById(currentUserId) : null;

        if (!photoPermissionService.canRead(photo, currentUser)) {
            throw new AccessDeniedException("Vous n'avez pas le droit de voir cette photo");
        }

        List<Partage> partages = partageRepository.findByPhotoId(photoId);
        return photoMapper.toPhotoDetailsDTO(photo, partages);
    }

    @Override
    public void updatePhoto(Long photoId, PhotoUpdateDTO dto, Long currentUserId) {
        Photo photo = getPhotoEntityById(photoId);
        User currentUser = getUserById(currentUserId);

        //Vérification des droits d'administration
        if (!photoPermissionService.canAdmin(photo, currentUser)) {
            throw new AccessDeniedException("Vous n'avez pas le droit de modifier cette photo");
        }

        photo.setTitle(dto.getTitle());
        photo.setDescription(dto.getDescription());
        photo.setVisibility(Visibility.valueOf(dto.getVisibility()));

        photoRepository.save(photo);
    }


    @Override
    public PhotoFileDTO getPhotoFileInfo(Long photoId, Long currentUserId) {
        Photo photo = getPhotoEntityById(photoId);
        User currentUser = currentUserId != null ? getUserById(currentUserId) : null;

        // Vérification des droits de lecture
        if (!photoPermissionService.canRead(photo, currentUser)) {
            throw new AccessDeniedException("Accès interdit à cette photo");
        }

        // Retourner un DTO avec les infos nécessaires pour ImageController
        return new PhotoFileDTO(
                photo.getStorageFilename(),
                photo.getContentType()
        );
    }

    @Override
    public byte[] loadPhotoFile(String storageFilename) {
        return photoStorageService.loadPhoto(storageFilename);
    }

    @Override
    public byte[] loadPhotoThumbnail(String storageFilename) {
        return photoStorageService.loadThumbnail(storageFilename);
    }

    private Photo getPhotoEntityById(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new PhotoNotFoundException(id));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable (id=" + id + ")"));
    }
}
