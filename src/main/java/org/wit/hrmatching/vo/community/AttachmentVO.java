package org.wit.hrmatching.vo.community;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttachmentVO {
    private Long id;
    private Long postId;
    private String originalName;
    private String storedName;
    private Long fileSize;
    private LocalDateTime uploadedAt;
}

