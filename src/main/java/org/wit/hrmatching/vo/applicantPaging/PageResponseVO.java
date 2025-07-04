package org.wit.hrmatching.vo.applicantPaging;

import lombok.Data;

import java.util.List;

@Data
public class PageResponseVO<T> {

	private List<T> content;      // 현재 페이지에 포함된 데이터 목록
	private PagingInfo pagingInfo;    // 페이징에 필요한 모든 정보를 담는 객체

	private int countInProgress; // '진행 중' 상태의 전체 개수
	private int countFinal;      // '최종 발표' 상태의 전체 개수

	public PageResponseVO(int totalRecord, SearchCriteria criteria, List<T> content, int countInProgress, int countFinal) {
		this.content = content;
		this.pagingInfo = new PagingInfo(totalRecord, criteria);
		this.countInProgress = countInProgress;
		this.countFinal = countFinal;
	}

	public PageResponseVO(int totalRecord, SearchCriteria criteria, List<T> content) {
		this(totalRecord, criteria, content, 0, 0);
	}
}
