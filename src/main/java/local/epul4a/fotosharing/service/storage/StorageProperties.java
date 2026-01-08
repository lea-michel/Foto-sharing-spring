package local.epul4a.fotosharing.service.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Lit les propriétés du stockage dans application.properties.
 */
@Component
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    /**
     * Répertoire où stocker les fichiers.
     * Valeur par défaut : "uploads".
     */
    private String location = "uploads";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
