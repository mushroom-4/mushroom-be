package nbc.mushroom.domain.common.util.image;

import org.springframework.web.multipart.MultipartFile;

public class ImageUtilS3 implements ImageUtil {

    @Override
    public String upload(MultipartFile image) {
        return "";
    }

    @Override
    public void delete(String filename) {

    }

    @Override
    public String getImageUrl(String filename) {
        return "";
    }
}
