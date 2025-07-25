package org.wit.hrmatching.controller.community;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.service.community.BoardService;
import org.wit.hrmatching.vo.community.CommentVO;
import org.wit.hrmatching.vo.community.PostVO;
import org.wit.hrmatching.vo.user.CustomUserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/community/comments")
public class CommunityCommentController {

    private final BoardService boardService;

    // 댓글 목록 조회 (대댓글 포함)
    @GetMapping("/{postId}")
    public List<CommentVO> getComments(@PathVariable Long postId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        PostVO post = boardService.getPostById(postId);
        Long writerId = post.getUserId();
        Long currentUserId = (userDetails != null) ? userDetails.getId() : null;

        return boardService.getCommentsWithReplies(postId, writerId, currentUserId);
    }



    // 댓글 등록
    @PostMapping
    public void createComment(@RequestBody CommentVO comment,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        comment.setUserId(userDetails.getId());
        boardService.addComment(comment);
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public void updateComment(@PathVariable Long id, @RequestBody CommentVO comment) {
        comment.setId(id);
        boardService.updateComment(comment);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        boardService.deleteComment(id);
    }

    @PostMapping("/{commentId}/like")
    public Map<String, Object> toggleCommentLike(@PathVariable Long commentId,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        boolean liked = boardService.toggleCommentLike(commentId, userDetails.getId());
        int newCount = boardService.getCommentLikeCount(commentId);

        Map<String, Object> response = new HashMap<>();
        response.put("liked", liked);
        response.put("likeCount", newCount);
        return response;
    }

}
