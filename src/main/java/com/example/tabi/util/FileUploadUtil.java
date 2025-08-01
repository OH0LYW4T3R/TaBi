package com.example.tabi.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class FileUploadUtil {
    private static final String ROOT_DIR = System.getProperty("user.dir") + "/uploads/";

    private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".gif", ".webp");
    private static final List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "image/gif", "image/webp");

    public static String saveImage(MultipartFile file, String userId, String questType) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("허용되지 않은 이미지 확장자입니다: " + extension);
        }

        String mimeType = file.getContentType();
        if (mimeType == null || !ALLOWED_MIME_TYPES.contains(mimeType.toLowerCase())) {
            throw new IllegalArgumentException("이미지 MIME 타입이 유효하지 않습니다: " + mimeType);
        }

        String uuidFileName = UUID.randomUUID() + extension;

        String relativePath = userId + "/" + questType + "/";
        String fullPath = ROOT_DIR + relativePath;

        File directory = new File(fullPath);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("디렉토리 생성 실패: " + fullPath);
        }

        File dest = new File(fullPath + uuidFileName);
        file.transferTo(dest);

        return "/uploads/" + relativePath + uuidFileName;
    }
}
