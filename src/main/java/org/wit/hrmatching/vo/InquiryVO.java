package org.wit.hrmatching.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class InquiryVO {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String reply;
    private LocalDateTime repliedAt;

    private String name;
    private String email;
}
