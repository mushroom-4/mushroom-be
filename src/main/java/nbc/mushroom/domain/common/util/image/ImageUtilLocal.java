package nbc.mushroom.domain.common.util.image;

import static nbc.mushroom.domain.common.exception.ExceptionType.SERVER_IMAGE_FAIL;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ImageUtilLocal implements ImageUtil {

    @Value("${image.file.endpoint}")
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
        String filename = UUID.randomUUID() + ext; // "랜덤파일명.확장자"

        try {
            Path path = Paths.get("src/main/webapp/images/");
            Files.createDirectories(path);

            Path filePath = path.resolve(filename);

            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filename;
        } catch (IOException e) {
            throw new CustomException(SERVER_IMAGE_FAIL);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            File file = new File("src/main/webapp/images/" + filename);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            throw new CustomException(SERVER_IMAGE_FAIL);
        }
    }

    @Override
    public String getImageUrl(String filename) {
        return endpoint + "/" + filename;
    }
}
