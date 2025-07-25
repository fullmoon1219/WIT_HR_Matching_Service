package org.wit.hrmatching.controller.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.dto.applicant.BookmarkDTO;
import org.wit.hrmatching.service.applicant.BookmarkService;
import org.wit.hrmatching.vo.bookmark.BookmarkListVO;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.vo.applicantPaging.PageResponseVO;
import org.wit.hrmatching.vo.applicantPaging.SearchCriteria;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkRestController {

	private final BookmarkService bookmarkService;

	@PostMapping("")
	public ResponseEntity<?> addBookmark(@RequestBody BookmarkDTO bookmarkDTO,
										 @AuthenticationPrincipal CustomUserDetails userDetails) {

		long userId = userDetails.getId();
		long jobPostId = bookmarkDTO.getJobPostId();

		boolean result = bookmarkService.addBookmark(userId, jobPostId);

		if (result) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{jobPostId}")
	public ResponseEntity<?> deleteBookmark(@PathVariable long jobPostId,
											@AuthenticationPrincipal CustomUserDetails userDetails) {

		long userId = userDetails.getId();

		boolean result = bookmarkService.deleteBookmark(userId, jobPostId);

		if (result) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("")
	public ResponseEntity<?> getBookmarkList(@AuthenticationPrincipal CustomUserDetails userDetails,
												@ModelAttribute SearchCriteria criteria) {

		long userId = userDetails.getId();

		PageResponseVO<BookmarkListVO> responseVO = bookmarkService.getBookmarkList(userId, criteria);

		return ResponseEntity.ok(responseVO);
	}
}
