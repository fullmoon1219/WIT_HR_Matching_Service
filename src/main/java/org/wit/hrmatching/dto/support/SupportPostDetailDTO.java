package org.wit.hrmatching.dto.support;

import lombok.Data;

@Data
public class SupportPostDetailDTO {
    private Long id;
    private Long userId;
    private String nickname;
    private String title;
    private String content;
    private String createdAt;
    private int commentCount;
    private int likeCount;
    private boolean deleted;
}
