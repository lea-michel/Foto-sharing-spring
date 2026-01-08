package local.epul4a.fotosharing.service.exception;

public class PhotoNotFoundException extends RuntimeException {
    public PhotoNotFoundException(Long id) {
        super("Photo introuvable (id=" + id + ")");
    }
}
