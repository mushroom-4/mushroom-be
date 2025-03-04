package nbc.mushroom.domain.common.util.image;

import static nbc.mushroom.domain.common.exception.ExceptionType.SERVER_IMAGE_FAIL;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Primary
@Component
@RequiredArgsConstructor
public class ImageUtilS3 implements ImageUtil {

    private final S3Client s3Client;

    @Value("${image.s3.bucket}")
    private String bucketName;

    @Value("${image.s3.endpoint}")
    private String endpoint;

    @Override
    public String upload(MultipartFile image) {
        if (image == null) {
            return null;
        }

        String ext = "";
        int i = image.getOriginalFilename().lastIndexOf('.');
        if (i > 0) {
            ext = image.getOriginalFilename().substring(i);
        }
        String filename = UUID.randomUUID() + ext;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key("public/" + filename)
                .contentType(image.getContentType())
                .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(image.getBytes()));
            return filename;
        } catch (IOException e) {
            throw new CustomException(SERVER_IMAGE_FAIL);
        }
    }

    @Override
    public void delete(String filename) {
        if (filename == null) {
            return;
        }
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key("public/" + filename)
                .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new CustomException(SERVER_IMAGE_FAIL);
        }
    }

    @Override
    public String getImageUrl(String filename) {
        if (filename == null) {
            return null;
        }
        return endpoint + "/" + filename;
    }
}
