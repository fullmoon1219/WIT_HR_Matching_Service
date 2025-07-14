package org.wit.hrmatching.service.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wit.hrmatching.dao.applicant.BookmarkDAO;
import org.wit.hrmatching.vo.bookmark.BookmarkListVO;
import org.wit.hrmatching.vo.applicantPaging.PageResponseVO;
import org.wit.hrmatching.vo.applicantPaging.SearchCriteria;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {
	
	private final BookmarkDAO bookmarkDAO;

	public boolean checkBookmarkExist(long userId, long jobPostId) {

		return bookmarkDAO.checkBookmarkCount(userId, jobPostId) > 0;
	}

	@Transactional
	public boolean addBookmark(long userId, long jobPostId) {

		if (bookmarkDAO.addBookmark(userId, jobPostId)) {
			return bookmarkDAO.increaseBookmarkCount(jobPostId);
		}

		return false;
	}

	@Transactional
	public boolean deleteBookmark(long userId, long jobPostId) {

		if (bookmarkDAO.deleteBookmark(userId, jobPostId)) {
			return bookmarkDAO.decreaseBookmarkCount(jobPostId);
		}

		return false;
	}

	public PageResponseVO<BookmarkListVO> getBookmarkList(long userId, SearchCriteria criteria) {

		int totalRecord = bookmarkDAO.countBookmarkList(userId, criteria);

		if (totalRecord < 1) {
			return new PageResponseVO<>(0, criteria, Collections.emptyList());
		}

		List<BookmarkListVO> content = bookmarkDAO.getBookmarkList(userId, criteria);

		return new PageResponseVO<>(totalRecord, criteria, content);
	}
}
