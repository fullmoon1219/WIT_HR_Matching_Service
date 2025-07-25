package org.wit.hrmatching.vo.community;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostVO {
    private Long id;
    private Long boardId;
    private Long userId;
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private BoardVO board;
}
