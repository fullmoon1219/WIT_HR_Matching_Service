// applicant/recruit/list.html

let currentFilters = {
	region: '',
	techStacks: [],
	keyword: '',
	page: 1,
	sortOrder: 'latest'
}

$(document).ready(function () {

	loadRecruitList();

	$('.filter-toggle').on('click', function () {

		const currentWrapper = $(this).closest('.filter-wrapper');

		currentWrapper.siblings('.filter-toggle').removeClass('active');
		currentWrapper.toggleClass('active');

	});

});

function loadRecruitList(currentFilters) {

	$.ajax({
		url: "/api/recruit",
		type: "GET",
		data: currentFilters,
		success: function (response) {

			const tbody = $('#recruit_list');
			tbody.empty();

			const recruits = response.content;
			const pagingInfo = response.pagingInfo;

			if (!recruits || recruits.length === 0) {
				const emptyRow = `<tr><td colspan="5" style="text-align: center;">공고가 없습니다.</td></tr>`;
				tbody.append(emptyRow);
			} else {
				recruits.forEach(function(recruit) {
					const row = makeRow(recruit);
					tbody.append(row);
				});
			}

			// 페이징 UI 그리는 함수 호출
			renderPagination(pagingInfo);

			const totalRecord = pagingInfo.totalRecord;
			$('#totalRecord').html(totalRecord);

		},
		error: function (xhr) {
			if (xhr.status === 403) {
				location.href = '/error/access-denied';
			} else if (xhr.status === 404) {
				location.href = '/error/not-found';
			} else {
				alert('스크랩 목록 불러오기에 실패했습니다. 나중에 다시 시도해주세요.')
				console.error("스크랩 목록 로딩 중 오류 발생:", xhr);
			}
		}
	});
}

function makeRow(recruit) {

	return '';
}