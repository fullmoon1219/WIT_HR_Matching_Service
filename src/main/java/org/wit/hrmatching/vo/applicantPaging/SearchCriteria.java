package org.wit.hrmatching.vo.applicantPaging;

import lombok.Data;

import java.util.List;

@Data
public class SearchCriteria {

	private int page;             			// 현재 페이지 번호
	private int recordPerPage;    			// 페이지당 보여줄 데이터 개수
	private String searchType;    			// 검색 유형 (예: 제목, 작성자) - 필요 시 사용
	private String keyword;       			// 검색어
	private String regionKeyword;			// 지역 전용 검색어
	private List<String> status;  			// 지원 상태 필터링 (예: 'APPLIED', 'ACCEPTED')
	private List<String> region;          	// 지역 필터
	private List<String> techStacks;    	// 기술 스택 필터 (다중 선택)
	private String sortOrder;     			// 정렬 순서 (예: 'latest', 'deadline')
	private List<String> employmentTypes;	// 고용 형태 필터 (다중 선택)
	private List<String> experienceTypes;	// 경력 타입
	private boolean salaryOnly;				// 연봉정보 필터링 (예: 3000, 4000만원)

	public SearchCriteria() {
		this.page = 1;
		this.recordPerPage = 10;
	}

	public int getSkip() {
		return (page - 1) * recordPerPage;
	}
}
