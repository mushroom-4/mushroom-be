package nbc.mushroom.domain.common.util.image;

import static nbc.mushroom.domain.common.exception.ExceptionType.IMAGE_UPLOAD_FAIL;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import nbc.mushroom.domain.common.exception.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageLocal implements Image {

    @Override
    public String upload(MultipartFile image) {

        if (image == null) {
            return null;
        }
        try {
            String ext = "";
            int i = image.getOriginalFilename().lastIndexOf('.');
            if (i > 0) {
                ext = image.getOriginalFilename().substring(i);
            }
            String filename = UUID.randomUUID() + ext; // "랜덤파일명.확장자"

            Path path = Paths.get("src/main/webapp/images/");

            Files.createDirectories(path);

            Path filePath = path.resolve(filename);

            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/images/" + filename;
        } catch (IOException e) {
            throw new CustomException(IMAGE_UPLOAD_FAIL);
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
            throw new CustomException(IMAGE_UPLOAD_FAIL);
        }
    }

    @Override
    public String getImageUrl(String filename) {
        return "http://localhost:8080/images/" + filename;
    }
}
