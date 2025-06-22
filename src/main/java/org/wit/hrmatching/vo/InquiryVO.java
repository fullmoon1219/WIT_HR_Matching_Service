package org.wit.hrmatching.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InquiryVO {
    private Long id;
    private Long userId;
    private String category;
    private String title;
    private String content;
    private String answer;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;
}
