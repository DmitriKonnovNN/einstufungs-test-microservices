package solutions.dmitrikonnov.etverwaltung.s3;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class S3FileStoreService {

    private final AmazonS3 amazonS3;

    @Async
    public void upload(String path,
                       String fileName,
                       Optional<Map<String, String>> optionalMetaData,
                       InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        try {
            amazonS3.putObject(path, fileName, inputStream, objectMetadata);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload the file", e);
        }
    }

    public byte[] download(String path, String fileName) {
        try {
            S3Object object = amazonS3.getObject(path, fileName);
            S3ObjectInputStream objectContent = object.getObjectContent();
            return IOUtils.toByteArray(objectContent);
        } catch (AmazonServiceException | IOException e) {

            throw new IllegalStateException("Failed to download the file", e);
        }
    }

    public void delete (String fileName){
        try {
            amazonS3.deleteObject(BucketName.AUFGABE_MEDIADATA.getBucketName(), fileName);
        }
        catch (AmazonServiceException e){
            throw new IllegalStateException("Deletion failed!", e);
        }
    }

}
