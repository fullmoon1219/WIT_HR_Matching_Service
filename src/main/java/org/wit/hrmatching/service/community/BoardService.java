package org.wit.hrmatching.service.community;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.wit.hrmatching.config.file.FileUploadProperties;
import org.wit.hrmatching.dto.admin.PagedResponseDTO;
import org.wit.hrmatching.dto.community.CommunityPostListDTO;
import org.wit.hrmatching.dto.community.PostDetailDTO;
import org.wit.hrmatching.mapper.community.BoardMapper;
import org.wit.hrmatching.vo.community.AttachmentVO;
import org.wit.hrmatching.vo.community.BoardVO;
import org.wit.hrmatching.vo.community.CommentVO;
import org.wit.hrmatching.vo.community.PostVO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;
    private final FileUploadProperties fileUploadProperties;

    public List<BoardVO> findAllBoards() {
        return boardMapper.findAll();
    }

    public void writePostWithFiles(PostVO postVO, MultipartFile[] files) {
        boardMapper.insertPost(postVO);

        if (files != null) {
            for (MultipartFile file : files) {
                String originalName = file.getOriginalFilename();

                if (originalName == null || originalName.trim().isEmpty()) {
                    System.out.println("⚠️ 원본 파일명이 null 혹은 빈 문자열이라 건너뜀");
                    continue;
                }

                try {
                    originalName = Paths.get(originalName).getFileName().toString();
                    String storedName = UUID.randomUUID() + "_" + originalName;

                    String baseDir = fileUploadProperties.getPostDir().replaceAll("[/\\\\]?$", "/");
                    String uploadPath = baseDir + storedName;

                    // 디렉토리 확인 및 생성
                    File targetFile = new File(uploadPath);
                    File parentDir = targetFile.getParentFile();
                    if (!parentDir.exists() && !parentDir.mkdirs()) {
                        throw new RuntimeException("업로드 디렉토리 생성 실패: " + parentDir.getAbsolutePath());
                    }

                    // 파일 저장
                    file.transferTo(targetFile);
                    System.out.println("✅ 저장 성공: " + uploadPath);

                    // DB 기록
                    AttachmentVO attachment = new AttachmentVO();
                    attachment.setPostId(postVO.getId());
                    attachment.setOriginalName(originalName);
                    attachment.setStoredName(storedName);
                    attachment.setFileSize(file.getSize());

                    boardMapper.insertAttachment(attachment);

                } catch (IOException e) {
                    System.err.println("❌ 파일 저장 중 예외 발생");
                    throw new RuntimeException("파일 업로드 실패: " + originalName, e);
                }
            }
        }
    }

    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 비어있습니다.");
        }

        String originalName = file.getOriginalFilename();
        String storedName = UUID.randomUUID() + "_" + originalName;
        String uploadPath = fileUploadProperties.getPostImageDir() + storedName;

        try {
            File targetFile = new File(uploadPath);
            File parentDir = targetFile.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                throw new RuntimeException("이미지 업로드 디렉토리 생성 실패: " + parentDir.getAbsolutePath());
            }

            file.transferTo(targetFile);
            return "/uploads/posts/" + storedName; // 웹에서 접근 가능한 경로
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 실패", e);
        }
    }

    public void deleteImagesByUrl(List<String> imageUrls) {
        for (String url : imageUrls) {
            String filename = Paths.get(url).getFileName().toString();
            File file = new File(fileUploadProperties.getPostImageDir(), filename);
            if (file.exists()) {
                boolean deleted = file.delete();
            }
        }
    }

    public PagedResponseDTO<CommunityPostListDTO> getPagedPosts(int page, int size, String boardCode, Long userId) {
        int offset = page * size;

        List<CommunityPostListDTO> posts = boardMapper.getCommunityPostList(offset, size, boardCode, userId);
        long totalElements = boardMapper.getCommunityPostCount(boardCode);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return PagedResponseDTO.<CommunityPostListDTO>builder()
                .content(posts)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .size(size)
                .number(page)
                .first(page == 0)
                .last(page >= totalPages - 1)
                .numberOfElements(posts.size())
                .empty(posts.isEmpty())
                .build();
    }

    public PostDetailDTO getPostDetail(Long postId, Long userId) {
        PostDetailDTO post = boardMapper.selectPostDetail(postId);
        if (post == null) {
            throw new NoSuchElementException("해당 게시글이 존재하지 않습니다.");
        }

        List<AttachmentVO> attachments = boardMapper.selectAttachmentsByPostId(postId);
        List<CommentVO> flatComments = boardMapper.selectCommentsByPostId(postId);

        for (CommentVO comment : flatComments) {
            comment.setIsWriter(post.getWriterName().equals(comment.getWriterName()));
        }

        List<CommentVO> commentTree = buildCommentTree(flatComments);
        post.setAttachments(attachments);
        post.setComments(commentTree);

        // ✅ 좋아요 여부 추가
        boolean liked = boardMapper.hasUserLikedPost(postId, userId);
        post.setLiked(liked);

        return post;
    }


    private List<CommentVO> buildCommentTree(List<CommentVO> comments) {
        Map<Long, CommentVO> map = new HashMap<>();
        List<CommentVO> rootList = new ArrayList<>();

        for (CommentVO c : comments) {
            map.put(c.getId(), c);
        }

        for (CommentVO c : comments) {
            if (c.getParentId() == null) {
                rootList.add(c);
            } else {
                CommentVO parent = map.get(c.getParentId());
                if (parent != null) {
                    parent.getChildren().add(c);
                }
            }
        }

        return rootList;
    }

    public void increaseViewCount(Long postId) {
        boardMapper.incrementViewCount(postId);
    }

    @Transactional
    public boolean togglePostLike(Long postId, Long userId) {
        if (boardMapper.hasUserLikedPost(postId, userId)) {
            // 좋아요 취소
            boardMapper.deletePostLike(postId, userId);
            boardMapper.decreasePostLikeCount(postId);
            return false;
        } else {
            // 좋아요 등록
            boardMapper.insertPostLike(postId, userId);
            boardMapper.increasePostLikeCount(postId);
            return true;
        }
    }

    public int getPostLikeCount(Long postId) {
        return boardMapper.countPostLikes(postId);
    }

}
