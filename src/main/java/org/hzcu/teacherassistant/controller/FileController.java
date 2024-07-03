package org.hzcu.teacherassistant.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.hzcu.teacherassistant.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@Tag(name = "文件管理")
public class FileController {

    @Autowired
    private Environment env;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result upload(MultipartFile file) throws IOException {
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return Result.error("Invalid file name");
        }

        // 生成唯一目录名
        String uniqueDir = UUID.randomUUID().toString();

        // 获取 classpath 下的 upload 目录路径
        ClassPathResource resource = new ClassPathResource("");
        String uploadDirPath = resource.getFile().getAbsolutePath() + "/upload/" + uniqueDir + "/";

        // 创建文件对象
        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        File localFile = new File(uploadDir, originalFilename);

        // 将文件写入本地存储
        try (FileOutputStream fos = new FileOutputStream(localFile)) {
            fos.write(file.getBytes());
        }

        // 返回成功结果
        return Result.success(uniqueDir + "/" + originalFilename);
    }
    // 下载接口

    @GetMapping("/download/{uniqueDir}/{filename}")
    @Operation(summary = "下载文件")
    public ResponseEntity<Resource> downloadFile(@PathVariable String uniqueDir, @PathVariable String filename, HttpServletRequest request) throws IOException {
        // 获取 classpath 下的 upload 目录路径
        ClassPathResource resource = new ClassPathResource("");
        String uploadDirPath = resource.getFile().getAbsolutePath() + "/upload/" + uniqueDir + "/";

        // 创建文件对象
        File file = new File(uploadDirPath, filename);
        if (!file.exists()) {
            return ResponseEntity.status(404).body(null);
        }

        // 加载文件资源
        Resource fileResource = new FileSystemResource(file);

        // 动态获取运行时端口
        String runtimePort = env.getProperty("local.server.port");
        if (runtimePort == null || runtimePort.isEmpty()) {
            runtimePort = request.getServerPort() != 0 ? String.valueOf(request.getServerPort()) : env.getProperty("server.port", "8080");
        }

        String downloadUrl = "http://" + request.getServerName() + ":" + runtimePort + "/files/download/" + uniqueDir + "/" + filename;

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add("Download-URL", downloadUrl);

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileResource);
    }
}
