// applicant/recruit/list.html

let currentFilters = {
	region: '',
	techStacks: [],
	keyword: '',
	regionKeyword: '',
	page: 1,
	sortOrder: 'latest'
}

let totalPage = 1;

$(document).ready(function () {

	loadRecruitList(currentFilters);

	$('.filter-toggle').on('click', function () {

		const dataType = $(this).data('type');
		const targetDropdown = $(`#${dataType}-dropdown`);

		$(this).toggleClass('active');
		$('.filter-toggle').not(this).removeClass('active');
		$('.filter-dropdown').not(targetDropdown).hide();

		targetDropdown.toggle();
	});

	$(document).on('click', '.recruit-card', function(e) {

		// 이중으로 창이 열리는 것을 방지
		if ($(e.target).is('a, a *')) {
			return;
		}

		const jobPostId = $(this).data('jobpost-id');

		if (jobPostId) {
			window.open(`/recruit/view/${jobPostId}`, '_blank');
		}
	});

	$('#pagination').on('click', 'a', function(e) {
		e.preventDefault();

		const page = $(this).data('page');

		currentFilters.page = page;
		loadRecruitList(currentFilters);
	});

	$('#go-to-page-btn').on('click', function () {

		const input = $('#direct-page-input').val().trim();
		const targetPage = parseInt(input, 10);

		if (isNaN(targetPage) || targetPage < 1 || targetPage > totalPage) {
			alert(`1부터 ${totalPage} 사이의 페이지를 입력하세요.`);
			return;
		}

		currentFilters.page = targetPage;
		loadRecruitList(currentFilters);

		$('#direct-page-input').val('');
	});

	$('#sort-order-select').on('change', function () {

		currentFilters.page = 1;
		currentFilters.sortOrder = $(this).val();

		loadRecruitList(currentFilters);
	});

	$('#reset-filters-btn').on('click', function() {

		currentFilters = {
			region: '',
			techStacks: [],
			keyword: '',
			regionKeyword: '',
			techStackKeyword: '',
			page: 1,
			sortOrder: 'latest'
		};

		$('.filter-options button').removeClass('active');
		$('#main-search').val('');

		$('#sort-order-select').val('latest');

		loadRecruitList(currentFilters);
	});

	$('#region-options').on('click', 'button', function () {

		const clickedButton = $(this);
		const dataValue = $(this).data('value');

		if (clickedButton.hasClass('active')) {
			clickedButton.removeClass('active');
			currentFilters.region = '';

		} else {
			clickedButton.addClass('active');
			clickedButton.siblings().removeClass('active');
			currentFilters.region = dataValue;
		}

		currentFilters.page = 1;
		currentFilters.sortOrder = 'latest';

		$('#sort-order-select').val('latest');

		loadRecruitList(currentFilters);
	});

	$('#stack-options').on('click', 'button', function () {

		const clickedButton = $(this);
		const dataValue = $(this).data('value');

		clickedButton.toggleClass('active');

		if (currentFilters.techStacks.includes(dataValue)) {
			const index = currentFilters.techStacks.indexOf(dataValue);
			currentFilters.techStacks.splice(index, 1);

		} else {
			currentFilters.techStacks.push(dataValue);
		}

		currentFilters.page = 1;
		currentFilters.sortOrder = 'latest';

		$('#sort-order-select').val('latest');

		loadRecruitList(currentFilters);
	});

	$('#main-search-wrapper').on('click', 'button', function () {
		performSearch();
		$('#sort-order-select').val('latest');
	});

	$('#main-search').on('keyup', function (e) {
		if (e.key === 'Enter') {
			performSearch();
		}
		$('#sort-order-select').val('latest');
	});

	$('.filter-search-wrapper').on('click', 'span', function () {
		performRegionSearch();
		$('#sort-order-select').val('latest');
	});

	$('.filter-search').on('keyup', function (e) {
		if (e.key === 'Enter') {
			performRegionSearch();
		}
		$('#sort-order-select').val('latest');
	});
});

function loadRecruitList(currentFilters) {

	$.ajax({
		url: '/api/recruit',
		type: 'GET',
		data: currentFilters,
		success: function (response) {

			const tbody = $('#recruit-list');
			tbody.empty();

			const recruits = response.content;
			const pagingInfo = response.pagingInfo;

			if (!recruits || recruits.length === 0) {
				const emptyRow = '<div style="text-align: center;">공고가 없습니다.</div>';
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

			totalPage = response.pagingInfo.totalPage;
			$('#total-page-info').text(` / 총 ${totalPage}페이지`);

		},
		error: function (xhr) {
			if (xhr.status === 403) {
				location.href = '/error/access-denied';
			} else if (xhr.status === 404) {
				location.href = '/error/not-found';
			} else {
				alert('공고 목록 불러오기에 실패했습니다. 나중에 다시 시도해주세요.')
				console.error('공고 목록 로딩 중 오류 발생: ', xhr);
			}
		}
	});
}

function makeRow(recruit) {

	let tagsHtml = '';

	if (recruit.jobCategory && recruit.jobCategory.trim() !== '') {
		const tags = recruit.jobCategory.split(',');
		tags.forEach(tag => {
			tagsHtml += `<span class="tag">${tag.trim()}</span>`;
		});
	}

	const rowHtml = `
        <div class="recruit-card" data-jobpost-id="${recruit.jobPostId}">
            
            <div class="card-top">
                <div class="job-title-container">
                    <a href="/recruit/view/${recruit.jobPostId}" target="_blank">
                        <h3>${recruit.jobPostTitle}</h3>
                    </a>
                </div>
                <div class="company-name-container">
                    <span>${recruit.companyName}</span>
                </div>
            </div>

            <div class="card-middle">
                <span class="meta-item">🧭 ${recruit.location}</span>
                <span class="meta-item">📈 ${translateExperienceType(recruit.experienceType)}</span>
                <span class="meta-item">📄 ${translateEmploymentType(recruit.employmentType)}</span>
                <span class="meta-item">${formatSalary(recruit.salary)}</span>
            </div>

            <div class="card-bottom">
                <div class="tags">
                    ${tagsHtml}
                </div>
                <div class="dates">
                    <span>등록일 ${recruit.createAt}</span>
                    <span class="deadline">마감일 ${recruit.deadline}</span>
                </div>
            </div>
        </div>
    `;

	return rowHtml;
}

function formatSalary(salary) {

	// 비어있을 경우 빈 문자열 반환
	if (!salary || String(salary).trim() === '') {
		return '';
	}

	// 숫자 형태인지 확인 후 '만원' 추가
	if (!isNaN(parseFloat(salary)) && isFinite(salary)) {
		return `💰 ${salary}만원`;
	}

	// 숫자 형태가 아닌 경우
	return `💰 ${salary}`;
}

function performSearch() {

	const keyword = $('#main-search').val();

	currentFilters.keyword = keyword;
	currentFilters.page = 1;
	currentFilters.sortOrder = 'latest';
	currentFilters.techStacks = [];
	currentFilters.region = '';
	currentFilters.regionKeyword = '';

	$('.filter-options .filter-btn').removeClass('active');
	$('.filter-toggle').removeClass('active');

	$('.filter-dropdown').hide();

	$('#sort-order-select').val('latest');

	loadRecruitList(currentFilters);
}

function performRegionSearch() {

	const regionKeyword = $('.filter-search').val();

	currentFilters.regionKeyword = regionKeyword;
	currentFilters.keyword = '';
	currentFilters.page = 1;
	currentFilters.sortOrder = 'latest';
	currentFilters.region = '';

	$('#region-options .filter-btn').removeClass('active');

	$('#sort-order-select').val('latest');

	loadRecruitList(currentFilters);
}