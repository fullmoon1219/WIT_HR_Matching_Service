package org.wit.hrmatching.mapper.community;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.wit.hrmatching.dto.community.CommunityPostListDTO;
import org.wit.hrmatching.dto.community.PostDetailDTO;
import org.wit.hrmatching.vo.community.AttachmentVO;
import org.wit.hrmatching.vo.community.BoardVO;
import org.wit.hrmatching.vo.community.CommentVO;
import org.wit.hrmatching.vo.community.PostVO;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardVO> findAll();
    void insertPost(PostVO postVO);
    void insertAttachment(AttachmentVO attachmentVO);

    List<CommunityPostListDTO> getCommunityPostList(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("boardCode") String boardCode,
            @Param("userId") Long userId
    );


    long getCommunityPostCount(@Param("boardCode") String boardCode);

    PostDetailDTO selectPostDetail(@Param("postId") Long postId);

    List<AttachmentVO> selectAttachmentsByPostId(@Param("postId") Long postId);

    List<CommentVO> selectCommentsByPostId(@Param("postId") Long postId);

    void incrementViewCount(@Param("postId") Long postId);

    boolean hasUserLikedPost(@Param("postId") Long postId, @Param("userId") Long userId);
    void insertPostLike(@Param("postId") Long postId, @Param("userId") Long userId);
    void deletePostLike(@Param("postId") Long postId, @Param("userId") Long userId);
    int countPostLikes(@Param("postId") Long postId);

    void increasePostLikeCount(Long postId);
    void decreasePostLikeCount(Long postId);

}
