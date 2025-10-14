package kr.co.bnk_marketproject_be.service;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@Service
public class AdminStorageService {

    @Value("${app.upload.dir:upload}") // ✅ WebConfig와 동일 ("upload")
    private String uploadDir;

    /**
     * 파일을 저장하고, DB에는 '/upload/파일명' 형태로만 저장
     */
    public String saveAndReturnUrl(MultipartFile file, String prefix) throws Exception {
        // 확장자 추출
        String ext = Optional.ofNullable(file.getOriginalFilename())
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf(".")))
                .orElse(".png");

        // 파일명 생성
        String filename = prefix + "_" + System.currentTimeMillis() + ext;

        // 절대경로 생성 (WebConfig의 upload 폴더와 동일)
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
        Files.createDirectories(uploadPath);

        // 실제 저장 경로
        Path dest = uploadPath.resolve(filename);
        file.transferTo(dest.toFile());

        // DB에 저장할 상대경로 (이걸로 /NICHIYA/upload/** 매핑됨)
        String dbPath = "/upload/" + filename;

        // 로그 확인용
        log.info("✅ [AdminStorageService] 파일 저장 완료");
        log.info("📂 실제 저장 경로 : {}", dest.toAbsolutePath());
        log.info("🗄️ DB 저장 경로 : {}", dbPath);

        return dbPath;
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith("/upload")) return;

        try {
            Path path = Paths.get(uploadDir, fileUrl.replace("/upload/", ""));
            Files.deleteIfExists(path);
            log.info("🗑️ 삭제된 파일: {}", path);
        } catch (IOException e) {
            log.error("파일 삭제 중 오류", e);
        }
    }
}
