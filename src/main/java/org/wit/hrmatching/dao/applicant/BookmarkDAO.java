package org.wit.hrmatching.dao.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.applicant.BookmarkMapper;
import org.wit.hrmatching.vo.bookmark.BookmarkListVO;
import org.wit.hrmatching.vo.applicantPaging.SearchCriteria;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookmarkDAO {
	
	private final BookmarkMapper bookmarkMapper;

	public int checkBookmarkCount(long userId, long jobPostId) {
		return bookmarkMapper.checkBookmarkExist(userId, jobPostId);
	}

	public boolean addBookmark(long userId, long jobPostId) {
		return bookmarkMapper.insertBookmark(userId, jobPostId) == 1;
	}

	public boolean deleteBookmark(long userId, long jobPostId) {
		return bookmarkMapper.deleteBookmark(userId, jobPostId) == 1;
	}

	public boolean increaseBookmarkCount(long jobPostId) {
		return bookmarkMapper.increaseBookmarkCount(jobPostId) == 1;
	}

	public boolean decreaseBookmarkCount(long jobPostId) {
		return bookmarkMapper.decreaseBookmarkCount(jobPostId) == 1;
	}

	public int countBookmarkList(long userId, SearchCriteria criteria) {
		return bookmarkMapper.selectBookmarkListCount(userId, criteria);
	}

	public List<BookmarkListVO> getBookmarkList(long userId, SearchCriteria criteria) {
		return bookmarkMapper.selectBookmarkList(userId, criteria);
	}
}
