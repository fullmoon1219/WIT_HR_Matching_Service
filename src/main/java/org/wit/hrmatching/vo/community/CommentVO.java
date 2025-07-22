package org.wit.hrmatching.vo.community;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.ALWAYS)
@Data
public class CommentVO {
    private Long id;
    private Long postId;
    private Long userId;
    private Long parentId;
    private String content;
    private int likeCount;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private String profileImage;

    private String writerName;
    private String email;

    @JsonProperty("isWriter")
    private boolean isWriter;

    @JsonProperty("isLiked")
    private boolean liked;


    private List<CommentVO> children = new ArrayList<>();

    public void setIsWriter(boolean isWriter) {
        this.isWriter = isWriter;
    }

    @JsonProperty("isLiked")
    public boolean getIsLiked() {
        return this.liked;
    }

}
