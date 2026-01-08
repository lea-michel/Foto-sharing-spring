package local.epul4a.fotosharing.service.photo;

import jakarta.annotation.PostConstruct;
import local.epul4a.fotosharing.data.model.PhotoStorageInformation;
import local.epul4a.fotosharing.presentation.dto.PhotoUploadDTO;
import local.epul4a.fotosharing.service.exception.FileSizeExceededException;
import local.epul4a.fotosharing.service.exception.FileStorageException;
import local.epul4a.fotosharing.service.exception.InvalidMimeTypeException;
import local.epul4a.fotosharing.service.storage.StorageProperties;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

//gérer le stockage physique des fichiers (upload, lecture, suppression, création de miniatures).

@Service
public class PhotoStorageServiceImpl implements PhotoStorageService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/gif"
    );

    private final Path root;

    private final Tika tika = new Tika();

    public PhotoStorageServiceImpl(StorageProperties storageProperties) {
        this.root =
                Paths.get(storageProperties.getLocation())
                        .toAbsolutePath()
                        .normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(root.resolve("photos"));
            Files.createDirectories(root.resolve("thumbnails"));
        } catch (IOException e) {
            throw new FileStorageException("Impossible d'initialiser le stockage", e);
        }
    }

    @Override
    public PhotoStorageInformation save(PhotoUploadDTO photoUploadDTO, MultipartFile file) {

        try {

            if (file.isEmpty()) {
                throw new FileStorageException("Fichier vide", null);
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new FileSizeExceededException("Le fichier dépasse 10 MB");
            }

            String realMimeType = tika.detect(file.getInputStream());
            if (!ALLOWED_TYPES.contains(realMimeType)) {
                throw new InvalidMimeTypeException(
                        "Type de fichier non autorisé : " + realMimeType);
            }

            String extension = switch (realMimeType) {
                case "image/jpeg" -> ".jpg";
                case "image/png" -> ".png";
                case "image/gif" -> ".gif";
                default -> throw new InvalidMimeTypeException("Type inconnu");
            };

            String storageFileName = UUID.randomUUID().toString();

            String fullStorageFileName = storageFileName + extension;

            Path target = root.resolve("photos").resolve(fullStorageFileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            // Création de la miniature
            Path thumb = root.resolve("thumbnails").resolve(storageFileName + "_thumb.jpg");
            generateThumbnail(target, thumb);

            return new PhotoStorageInformation(
                    file.getOriginalFilename(),
                    fullStorageFileName,
                    realMimeType);

        } catch (IOException ex) {
            throw new FileStorageException("Erreur lors du stockage du fichier", ex);
        }
    }

    @Override
    public byte[] loadPhoto(String storageFilename) {
        try {
            Path file = root
                    .resolve("photos")
                    .resolve(storageFilename).normalize();

            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new FileStorageException("Impossible de lire l'image : ", e);
        }
    }

    @Override
    public void delete(String storageFilename) {
        try {
            Files.deleteIfExists(root.resolve("photos").resolve(storageFilename));

            // Extraire le nom sans extension pour le thumbnail
            String nameWithoutExt = storageFilename.substring(0, storageFilename.lastIndexOf('.'));
            Files.deleteIfExists(root.resolve("thumbnails").resolve(nameWithoutExt + "_thumb.jpg"));
        } catch (IOException ignored) {
        }
    }

    @Override
    public byte[] loadThumbnail(String storageFilename) {
        try {
            String nameWithoutExt = storageFilename.substring(0, storageFilename.lastIndexOf('.'));
            Path file = root.resolve("thumbnails").resolve(nameWithoutExt + "_thumb.jpg").normalize();
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new FileStorageException("Impossible de lire la miniature", e);
        }
    }


    private void generateThumbnail(Path source, Path thumbnailPath) throws IOException {
        Thumbnails.of(source.toFile())
                .size(300, 300)
                .keepAspectRatio(true)  //Garde les proportions
                .outputFormat("jpg")
                .outputQuality(0.85)
                .toFile(thumbnailPath.toFile());
    }

    @Override
    public String getFullImageUrl(String storageFilename) {
        return "/img/photos/" + storageFilename;
    }
}
