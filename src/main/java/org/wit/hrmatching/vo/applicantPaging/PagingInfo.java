package org.wit.hrmatching.vo.applicantPaging;

import lombok.Data;

@Data
public class PagingInfo {

	private int currentPage;        // 현재 페이지 번호
	private int recordPerPage;      // 페이지당 데이터 수
	private int pagePerBlock = 5; // 한 번에 보여줄 페이지 번호 블록의 크기 (예: 1~5, 6~10)

	private long totalRecord;         // 전체 데이터 수
	private int totalPage;          // 전체 페이지 수

	private int startPage;          // 현재 블록의 시작 페이지 번호
	private int endPage;            // 현재 블록의 끝 페이지 번호

	private boolean hasPrevBlock;   // '이전' 블록 이동 버튼 표시 여부
	private boolean hasNextBlock;   // '다음' 블록 이동 버튼 표시 여부

	public PagingInfo(int totalRecord, SearchCriteria criteria) {
		this.totalRecord = totalRecord;
		this.currentPage = criteria.getPage();
		this.recordPerPage = criteria.getRecordPerPage();

		// 전체 페이지 수 계산
		this.totalPage = ((int)totalRecord - 1) / this.recordPerPage + 1;

		// 현재 페이지 블록의 시작 페이지 번호 계산
		this.startPage = this.currentPage - (this.currentPage - 1) % this.pagePerBlock;

		// 현재 페이지 블록의 끝 페이지 번호 계산
		this.endPage = this.startPage + this.pagePerBlock - 1;

		// 만약 계산된 끝 페이지 번호가 실제 전체 페이지 수보다 크다면, 보정해준다.
		if (this.endPage > this.totalPage) {
			this.endPage = this.totalPage;
		}

		// '이전', '다음' 버튼 표시 여부 계산
		this.hasPrevBlock = this.startPage > 1;
		this.hasNextBlock = this.endPage < this.totalPage;
	}
}
