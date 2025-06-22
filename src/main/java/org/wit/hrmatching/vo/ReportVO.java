package org.wit.hrmatching.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportVO {
    private Long id;
    private Long reporterId;
    private Long targetId;
    private String targetType;
    private String reason;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

