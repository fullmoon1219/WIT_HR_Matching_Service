package org.wit.hrmatching.dto.community;

import lombok.Data;
import org.wit.hrmatching.vo.community.AttachmentVO;
import org.wit.hrmatching.vo.community.CommentVO;

import java.util.List;

@Data
public class PostDetailDTO {
    private Long id;
    private String boardCode;
    private String boardName;
    private String boardDescription;
    private String title;
    private String content;
    private String writerName;
    private Long writerId;
    private String createdAt;
    private int viewCount;
    private int likeCount;
    private boolean liked;
    private List<AttachmentVO> attachments;
    private List<CommentVO> comments;
}
