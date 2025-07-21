package org.wit.hrmatching.dto.support;

import lombok.Data;

@Data
public class UserDetailDTO {
    private Long id;
    private String name;
    private String email;
    private String role;        // APPLICANT / EMPLOYER
    private int warningCount;
    private boolean suspended;
    private String createdAt;

    // 추가: 신고된 댓글/게시물 내용 등 (옵션)
    private String reportedComment;
    private String reportedPostTitle;
    private String reportedPostContent;
}