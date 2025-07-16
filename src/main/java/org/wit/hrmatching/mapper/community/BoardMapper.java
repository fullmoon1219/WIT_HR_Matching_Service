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

    List<CommentVO> selectCommentsByPostId(@Param("postId") Long postId, @Param("userId") Long userId);

    void incrementViewCount(@Param("postId") Long postId);

    boolean hasUserLikedPost(@Param("postId") Long postId, @Param("userId") Long userId);
    void insertPostLike(@Param("postId") Long postId, @Param("userId") Long userId);
    void deletePostLike(@Param("postId") Long postId, @Param("userId") Long userId);
    int countPostLikes(@Param("postId") Long postId);

    void increasePostLikeCount(Long postId);
    void decreasePostLikeCount(Long postId);

    int softDeletePost(@Param("postId") Long postId, @Param("userId") Long userId);

    List<CommentVO> getParentComments(@Param("postId") Long postId, @Param("postWriterId") Long postWriterId);
    List<CommentVO> getChildComments(@Param("parentId") Long parentId, @Param("postWriterId") Long postWriterId);


    void insertComment(CommentVO comment);
    void deleteComment(Long id);
    void updateComment(CommentVO comment);

    PostVO selectPostWriter(Long postId);

    boolean hasUserLikedComment(@Param("commentId") Long commentId, @Param("userId") Long userId);
    void insertCommentLike(@Param("commentId") Long commentId, @Param("userId") Long userId);
    void deleteCommentLike(@Param("commentId") Long commentId, @Param("userId") Long userId);
    void increaseCommentLikeCount(Long commentId);
    void decreaseCommentLikeCount(Long commentId);

    int getCommentLikeCount(Long commentId);
    List<Long> getUserLikedCommentIds(@Param("postId") Long postId, @Param("userId") Long userId);

    AttachmentVO selectAttachmentById(Long id);

    PostVO selectPostWithBoard(Long postId);


    void updatePost(@Param("postId") Long postId,
                    @Param("title") String title,
                    @Param("boardId") Long boardId,
                    @Param("content") String content);

    List<Long> findAttachmentIdsByPostId(@Param("postId") Long postId);

    void deleteAttachmentsByIds(@Param("ids") List<Long> ids);
}
