package org.ether.filetransfer.controller;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/file")
public class FileTransferController {

    private File file;

    @GetMapping
    public ResponseEntity<Resource> getFile() {
        try {
            // 通过文件名获取文件路径
            Path filePath = Paths.get(file.getPath());
            Resource resource = new UrlResource(filePath.toUri());

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", file.getName());

            // 返回响应实体
            return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
        } catch (MalformedURLException e) {
            // 处理文件路径不正确的异常
            log.info("未找到文件 {}", file.getPath());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ResponseBody
    public void setFile(String path) {
        file = new File(path);
    }
}
