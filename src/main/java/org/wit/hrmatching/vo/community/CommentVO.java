package org.wit.hrmatching.vo.community;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommentVO {
    private Long id;
    private Long postId;
    private Long userId;
    private Long parentId; // null이면 최상위 댓글
    private String content;
    private int likeCount;
    private boolean isDeleted;
    private LocalDateTime createdAt;

    // 사용자 정보 (조인 결과 포함)
    private String writerName;
    private String email;
    private boolean isWriter; // 게시글 작성자인 경우 true

    // 자식 댓글
    private List<CommentVO> children = new ArrayList<>();

    public void setIsWriter(boolean isWriter) {
        this.isWriter = isWriter;
    }
}
