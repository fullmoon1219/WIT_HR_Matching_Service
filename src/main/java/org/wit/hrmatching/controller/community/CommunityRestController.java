package org.wit.hrmatching.controller.community;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wit.hrmatching.dto.admin.PagedResponseDTO;
import org.wit.hrmatching.dto.community.CommunityPostListDTO;
import org.wit.hrmatching.dto.community.PostDetailDTO;
import org.wit.hrmatching.service.community.BoardService;
import org.wit.hrmatching.vo.community.BoardVO;
import org.wit.hrmatching.vo.community.PostVO;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.vo.user.UserVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityRestController {

    private final BoardService boardService;

    @GetMapping
    public List<BoardVO> findAllBoards() {
        return boardService.findAllBoards();
    }

    @GetMapping("/posts")
    public PagedResponseDTO<CommunityPostListDTO> getCommunityPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String boardCode,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return boardService.getPagedPosts(page, size, boardCode, userDetails.getId());
    }


    @GetMapping("/posts/{id}")
    public PostDetailDTO getPostDetail(@PathVariable Long id,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        return boardService.getPostDetail(id, userDetails.getId());
    }

    @PostMapping("/posts/{postId}/view")
    public ResponseEntity<Void> increaseViewCount(@PathVariable Long postId) {
        boardService.increaseViewCount(postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardCode}/write")
    public Long handleWritePost(@PathVariable String boardCode,
                                @ModelAttribute PostVO postVO,
                                @ModelAttribute("userDetails") UserVO userVO,
                                @RequestParam(value = "files", required = false) MultipartFile[] files) {
        postVO.setUserId(userVO.getId());
        boardService.writePostWithFiles(postVO, files);
        return postVO.getId(); // 게시글 ID 반환
    }


    @PostMapping("/image-upload")
    public Map<String, Object> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            String imageUrl = boardService.uploadImage(file);
            return Map.of("success", 1, "url", imageUrl);
        } catch (RuntimeException e) {
            return Map.of("success", 0, "message", e.getMessage());
        }
    }

    @PostMapping("/delete-temp-images")
    public ResponseEntity<?> deleteTempImages(@RequestBody Map<String, List<String>> payload) {
        List<String> imageUrls = payload.get("images");
        boardService.deleteImagesByUrl(imageUrls);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long postId,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean liked = boardService.togglePostLike(postId, userDetails.getId());
        int likeCount = boardService.getPostLikeCount(postId);

        Map<String, Object> result = new HashMap<>();
        result.put("liked", liked);
        result.put("likeCount", likeCount);

        return ResponseEntity.ok(result);
    }
}
