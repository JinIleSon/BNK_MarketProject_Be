package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.ProductImagesDTO;
import kr.co.bnk_marketproject_be.dto.ProductRegDTO;
import kr.co.bnk_marketproject_be.service.ProductsRegService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminProductRegController {

    private final ProductsRegService adminProductsRegService;

    @GetMapping("/admin/productReg/list")
    public String list() {

        return "admin/admin_productReg";
    }

    @PostMapping("/admin/productReg/register")
    public String register(ProductRegDTO productRegDTO) throws IOException {
        log.info("productRegDTO={}", productRegDTO);

        MultipartFile file = productRegDTO.getFile();
        if (file != null && !file.isEmpty()) {
            // 파일명
            String original = Objects.requireNonNullElse(file.getOriginalFilename(), "upload.bin");
            String saveName = UUID.randomUUID() + "_" + original;

            // 1) 런타임에 즉시 서빙되는 경로 (개발중)
            Path runtimeDir = Paths.get(System.getProperty("user.dir"),
                    "target","classes","static","images").toAbsolutePath().normalize();
            Files.createDirectories(runtimeDir);

            // 2) 소스 보존용 (다음 빌드에도 남도록)
            Path sourceDir = Paths.get(System.getProperty("user.dir"),
                    "src","main","resources","static","images").toAbsolutePath().normalize();
            Files.createDirectories(sourceDir);

            // 스트림으로 두 군데에 저장
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, runtimeDir.resolve(saveName), StandardCopyOption.REPLACE_EXISTING);
            }
            // runtime에 저장된 파일을 src로도 복사 (또는 위에서 in을 두 번 열어도 됨)
            Files.copy(runtimeDir.resolve(saveName), sourceDir.resolve(saveName), StandardCopyOption.REPLACE_EXISTING);

            // 브라우저에서 접근할 URL (정적 경로와 일치)
            String url = "images/" + saveName;
            productRegDTO.setUrl(url);
        }

        adminProductsRegService.save(productRegDTO);
        return "redirect:/admin/productReg/list";
    }

    @PostMapping("/admin/productReg/delete")
    public String delete(int product_code) {
        adminProductsRegService.deleteProduct(product_code);
        return "redirect:/admin/product/list";
    }

}