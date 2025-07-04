package org.wit.hrmatching.mapper.applicant;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.vo.BookmarkListVO;
import org.wit.hrmatching.vo.applicantPaging.SearchCriteria;

import java.util.List;

@Mapper
public interface BookmarkMapper {

	int checkBookmarkExist(long userId, long jobPostId);

	int insertBookmark(long userId, long jobPostId);
	int increaseBookmarkCount(long jobPostId);

	int deleteBookmark(long userId, long jobPostId);
	int decreaseBookmarkCount(long jobPostId);

	int selectBookmarkListCount(@Param("userId") long userId, @Param("criteria") SearchCriteria criteria);
	List<BookmarkListVO> selectBookmarkList(@Param("userId") long userId, @Param("criteria") SearchCriteria criteria);
}
