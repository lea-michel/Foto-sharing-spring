package local.epul4a.fotosharing.data.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoStorageInformation {
    private String originalFilename;
    private String storageFilename;
    private String contentType;
}
