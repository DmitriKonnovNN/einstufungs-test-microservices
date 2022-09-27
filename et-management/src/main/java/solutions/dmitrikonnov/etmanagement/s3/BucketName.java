package solutions.dmitrikonnov.etmanagement.s3;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BucketName {
    AUFGABE_MEDIADATA ("elasticbeanstalk-us-east-2-067464467036/einstufungstest/media");
    private final String bucketName;
}
