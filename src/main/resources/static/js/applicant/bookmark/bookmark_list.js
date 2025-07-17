// applicant/bookmark/list.html

let currentCriteria = {
	page: 1,
	recordPerPage: 10,
	sortOrder: 'latest',
	keyword: ''
};

$(document).ready(function() {

	loadBookmarkList();

	$('#bookmarkListBody').on('click', '.jobPost', function() {

		const jobPostId = $(this).closest('tr.bookmark-row').attr('data-jobPost-id');

		if (jobPostId) {
			window.open('/recruit/view/' + jobPostId, '_blank');
		}
	});

	// 기존 페이징 이벤트는 버튼 스타일로 변경될 예정이므로 'a' 대신 '.page-btn'
	$('#pagination').on('click', '.page-btn', function(e) {
		e.preventDefault();

		const page = $(this).data('page');

		// 해당 페이지의 목록을 불러오도록 loadBookmarkList 함수를 호출.
		loadBookmarkList({ page: page });
	});

	$('#sort-order').on('change', function() {

		const selectedSortOrder = $(this).val();

		const newCriteria = {
			sortOrder: selectedSortOrder,
			page: 1
		};

		loadBookmarkList(newCriteria);
	});

	$('#bookmarkListBody').on('click', '.delete-bookmark-btn', function() {

		const jobPostId = $(this).closest('tr.bookmark-row').data('jobpostId');

		if (!jobPostId) {
			alert('잘못된 요청입니다.');
			return;
		}

		if (!confirm('정말로 이 스크랩을 삭제하시겠습니까?')) {
			return;
		}

		$.ajax({
			url: `/api/bookmarks/${jobPostId}`,
			method: 'DELETE',
			success: function () {

				Toastify({
					text: '스크랩이 삭제되었습니다.',
					duration: 2000,
					gravity: "top",
					position: "center",
					stopOnFocus: false,
					style: {
						background: "#6495ED",
						color: "#ffffff"
					}
				}).showToast();

				loadBookmarkList();
			},
			error: function (xhr) {
				if (xhr.status === 403) {
					location.href = '/error/access-denied';
				} else if (xhr.status === 404) {
					location.href = '/error/not-found';
				} else {
					alert('스크랩 삭제에 실패했습니다. 나중에 다시 시도해주세요.');
					console.error(xhr);
				}
			}
		});
	});

	$('#searchBtn').on('click', function() {

		const keyword = $('#searchKeyword').val();

		loadBookmarkList({ page: 1, keyword: keyword });
	});

	$('#resetBtn').on('click', function() {
		loadBookmarkList({ keyword: '', page: 1 });
	});
});

function loadBookmarkList(newCriteria = {}) {

	if (newCriteria.page === undefined) {
		newCriteria.page = 1;
	}
	// currentCriteria 객체에 newCriteria객체 덮어쓰기 (조건 추가시)
	Object.assign(currentCriteria, newCriteria);

	$.ajax({
		url: "/api/bookmarks",
		type: "GET",
		data: currentCriteria,
		success: function (response) {

			const tbody = $('#bookmarkListBody');
			tbody.empty();

			const bookmarks = response.content;
			const pagingInfo = response.pagingInfo;

			if (!bookmarks || bookmarks.length === 0) {
				const emptyRow = `<tr><td colspan="5" style="text-align: center;">스크랩 내역이 없습니다.</td></tr>`;
				tbody.append(emptyRow);
			} else {
				bookmarks.forEach(function(bookmark, index) {

					const rowNum = index + 1 + ((pagingInfo.currentPage - 1) * pagingInfo.recordPerPage);

					const row = makeRow(bookmark, rowNum);
					tbody.append(row);
				});
			}

			// 총 레코드 수 표시
			const totalRecord = pagingInfo.totalRecord;
			$('#totalRecord').html(totalRecord);

			// 페이징 UI 그리는 함수 호출
			renderPagination(pagingInfo);

		},
		error: function (xhr) {
			if (xhr.status === 403) {
				location.href = '/error/access-denied';
			} else if (xhr.status === 404) {
				location.href = '/error/not-found';
			} else {
				alert('스크랩 목록 불러오기에 실패했습니다. 나중에 다시 시도해주세요.');
				console.error("스크랩 목록 로딩 중 오류 발생:", xhr);
			}
		}
	});
}

function makeRow(bookmark, rowNumber) {

	// console.log("makeRow 함수로 전달된 bookmark 객체:", bookmark);

	return `
         <tr class="bookmark-row" data-jobPost-id="${bookmark.jobPostId}">
            <td>${rowNumber}</td>
            <td><span class="jobPost">${bookmark.jobPostTitle}</span></td>
            <td><span class="jobPost">${bookmark.companyName}</span></td>
            <td>${bookmark.deadline}</td>
            <td><button type="button" class="delete-bookmark-btn">삭제</button></td>
        </tr>
    `;
}

// ⭐ 페이징 버튼 디자인을 위한 renderPagination 함수
function renderPagination(pagingInfo) {
	const pagination = $('#pagination');
	pagination.empty();

	const currentPage = pagingInfo.currentPage || pagingInfo.page;
	const totalPage = pagingInfo.totalPage;

	if (totalPage <= 1) {
		return;  // 페이지가 1 이하이면 페이징 표시 안함
	}

	for (let i = 1; i <= totalPage; i++) {
		// 현재 페이지에 active 클래스 추가
		const activeClass = (i === currentPage) ? 'active' : '';
		pagination.append(`<button class="page-btn ${activeClass}" data-page="${i}">${i}</button>`);
	}
}
