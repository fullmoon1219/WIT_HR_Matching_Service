package org.wit.hrmatching.controller.file;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.service.community.BoardService;
import org.wit.hrmatching.vo.community.AttachmentVO;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.io.File;

@RestController
@RequestMapping("/files/download")
@RequiredArgsConstructor
public class FileDownloadController {

    private final BoardService boardService;

    @Value("${upload.post-dir}")
    private String postAttachmentDir;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        AttachmentVO attachment = boardService.getAttachmentById(id);

        if (attachment == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(postAttachmentDir + attachment.getStoredName());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);
        String encodedName = URLEncoder.encode(attachment.getOriginalName(), StandardCharsets.UTF_8)
                .replace("+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
