package org.wit.hrmatching.dto.community;

import lombok.Data;

@Data
public class CommunityPostListDTO {
    private Long id;
    private String boardName;
    private String title;
    private String writerName;
    private String updatedAt;
    private int viewCount;
    private int likeCount;
    private boolean liked;
    private int commentCount;
}
