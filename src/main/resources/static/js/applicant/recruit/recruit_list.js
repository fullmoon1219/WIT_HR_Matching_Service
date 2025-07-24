let currentFilters = {
	region: '',
	techStacks: [],
	keyword: '',
	regionKeyword: '',
	page: 1,
	sortOrder: 'latest',
	employmentTypes: [],
	experienceTypes: [],
	salaryOnly: false
};

let totalPage = 1;
let appliedFilters = {}; // 현재 적용된 필터를 저장할 객체

const deepCopy = (obj) => JSON.parse(JSON.stringify(obj));

$(document).ready(function () {

	const urlParams = new URLSearchParams(window.location.search);
	const sortParam = urlParams.get('sortOrder');
	const validSortOrders = ['latest', 'oldest', 'deadline', 'views', 'scraps'];

	if (sortParam && validSortOrders.includes(sortParam)) {
		currentFilters.sortOrder = sortParam;
		$('#sort-order-select').val(sortParam);
	}

	$('.filter-dropdown').hide();

	loadRecruitList(currentFilters);
	appliedFilters = deepCopy(currentFilters);

	$('.filter-toggle').on('click', function () {

		const dataType = $(this).data('type');
		const targetDropdown = $(`#${dataType}-dropdown`);

		$(this).toggleClass('active');
		$('.filter-toggle').not(this).removeClass('active');
		$('.filter-dropdown').not(targetDropdown).hide();

		targetDropdown.toggle();
	});

	$('#pagination').on('click', 'a[data-page]', function (e) {
		e.preventDefault();

		const page = $(this).data('page');
		currentFilters.page = page;

		loadRecruitList(currentFilters);
		window.scrollTo({ top: 0, behavior: 'smooth' });
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
		window.scrollTo({ top: 0, behavior: 'smooth' });
	});

	$('#sort-order-select').on('change', function () {

		currentFilters.page = 1;
		currentFilters.sortOrder = $(this).val();

		loadRecruitList(currentFilters);
	});

	// 선택 & 적용된 필터 초기화
	$('#reset-filters-btn').on('click', function () {

		currentFilters = {
			region: '',
			techStacks: [],
			keyword: '',
			regionKeyword: '',
			page: 1,
			sortOrder: 'latest',
			employmentTypes: [],
			experienceTypes: [],
			salaryOnly: false
		};

		// 드롭다운의 선택 영역도 초기화
		$('.filter-options button').removeClass('active');
		$('#main-search').val('');
		$('#sort-order-select').val('latest');
		$('#detail-dropdown .filter-btn').removeClass('active');
		$('#detail-dropdown input[type="checkbox"]').prop('checked', false);

		appliedFilters = deepCopy(currentFilters);
		loadRecruitList(currentFilters);
		renderSelectedFilterTags();
		updateApplyButtonState();
	});

	// 지역 필터링 선택 (목록 로딩 바로 안함)
	$('#region-options').on('click', 'button', function () {

		const clickedButton = $(this);
		const dataValue = $(this).data('value');

		if (clickedButton.hasClass('active')) {
			clickedButton.removeClass('active');
			currentFilters.region = '';
		} else {
			clickedButton.addClass('active').siblings().removeClass('active');
			currentFilters.region = dataValue;
		}

		currentFilters.page = 1;
		currentFilters.sortOrder = 'latest';

		$('#sort-order-select').val('latest');

		renderSelectedFilterTags();
		updateApplyButtonState();
	});

	// 기술 스택 필터링 선택 (목록 로딩 바로 안함, 중복 가능)
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

		renderSelectedFilterTags();
		updateApplyButtonState();
	});

	// 상세 조건 필터의 체크박스 변경 감지
	$('#detail-dropdown').on('change', 'input[type="checkbox"]', function() {
		const salaryOnly = $('#salary-only').is(':checked');
		currentFilters.salaryOnly = salaryOnly;

		renderSelectedFilterTags();
		updateApplyButtonState();
	});

	$('#detail-dropdown').on('click', '.filter-btn', function() {

		const clickedButton = $(this);
		const dataType = clickedButton.data('type');
		const dataValue = clickedButton.data('value');

		// 기술 스택처럼 여러 개 선택 가능
		clickedButton.toggleClass('active');

		if (currentFilters[dataType].includes(dataValue)) {
			const index = currentFilters[dataType].indexOf(dataValue);
			currentFilters[dataType].splice(index, 1);
		} else {
			currentFilters[dataType].push(dataValue);
		}

		renderSelectedFilterTags();
		updateApplyButtonState();
	});

	$('#main-search-wrapper').on('click', 'button', function () {
		performSearch();
	});

	$('#main-search').on('keyup', function (e) {
		if (e.key === 'Enter') performSearch();
	});

	$('.filter-search-wrapper').on('click', 'span', function () {
		performRegionSearch();
	});

	$('.filter-search').on('keyup', function (e) {
		if (e.key === 'Enter') performRegionSearch();
	});

	// 필터 적용 버튼 클릭 이벤트
	$('#apply-filters-btn').on('click', function() {

		currentFilters.page = 1; // 필터를 새로 적용하므로 1페이지부터 조회
		loadRecruitList(currentFilters);

		appliedFilters = deepCopy(currentFilters); // 현재 상태를 '적용된 필터'로 저장
		updateApplyButtonState();

		renderSelectedFilterTags();

		$('.filter-dropdown').hide();
		$('.filter-toggle').removeClass('active');
	});

	// 선택된 필터 태그의 x 버튼 클릭 시 태그 삭제
	$('#selected-tags-wrapper').on('click', '.remove-tag', function(e) {

		e.preventDefault();

		const tag = $(this).closest('.filter-tag');
		const type = tag.data('type');
		const value = tag.data('value');

		// 타입에 따라 currentFilters 객체와 UI를 업데이트
		if (type === 'keyword') {
			currentFilters.keyword = '';
			$('#main-search').val('');
		} else if (type === 'regionKeyword') {
			currentFilters.regionKeyword = '';
			$('.filter-search[data-type="region"]').val('');
		} else if (type === 'region') {
			currentFilters.region = '';
			$('#region-options .filter-btn.active').removeClass('active');
		} else if (type === 'techStacks') {
			currentFilters.techStacks = currentFilters.techStacks.filter(item => item != value);
			$(`#stack-options .filter-btn[data-value="${value}"]`).removeClass('active');
		} else if (type === 'employmentTypes') {
			currentFilters.employmentTypes = currentFilters.employmentTypes.filter(item => item != value);
			$(`#detail-dropdown .filter-btn[data-value="${value}"]`).removeClass('active');
		} else if (type === 'experienceTypes') {
			currentFilters.experienceTypes = currentFilters.experienceTypes.filter(item => item != value);
			$(`#detail-dropdown .filter-btn[data-value="${value}"]`).removeClass('active');
		} else if (type === 'salaryOnly') {
			currentFilters.salaryOnly = false;
			$('#salary-only').prop('checked', false);
		}

		// 이곳에서는 목록 로드는 x, 사용자가 '적용' 버튼을 눌러야 최종 반영
		// 그냥 함수 호출하면 동작이 꼬여 태그 삭제가 화면에 반영 안됨
		setTimeout(() => {
			renderSelectedFilterTags();
			updateApplyButtonState();
		}, 0);
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
				tbody.append('<div style="text-align: center;">공고가 없습니다.</div>');
			} else {
				recruits.forEach(function (recruit) {
					const row = makeRow(recruit);
					tbody.append(row);
				});
			}

			renderPagination(pagingInfo);
			$('#totalRecord').html(pagingInfo.totalRecord);

			totalPage = pagingInfo.totalPage;
			$('#total-page-info').text(` / ${totalPage}`);
		},
		error: function (xhr) {

			if (xhr.status === 403) location.href = '/error/access-denied';
			else if (xhr.status === 404) location.href = '/error/not-found';
			else {
				alert('공고 목록 불러오기에 실패했습니다. 나중에 다시 시도해주세요.');
				console.error('공고 목록 로딩 중 오류 발생: ', xhr);
			}
		}
	});
}

function makeRow(recruit) {

	const employmentTypeText = translateEmploymentType(recruit.employmentType);
	const salaryText = formatSalary(recruit.salary);
	const experienceText = translateExperienceType(recruit.experienceType);

	const deadlineDate = recruit.deadline ? `⏰ 마감일: ${recruit.deadline}` : '채용시 마감';

	return `
        <div class="recruit-card" data-jobpost-id="${recruit.jobPostId}">
            
            <div class="company-info-wrapper">
                <span class="company-name">${recruit.companyName}</span>
            </div>
            
            <div class="job-info-wrapper">
                <h3 class="job-title">
					<a href="/recruit/view/${recruit.jobPostId}" target="_blank">
						${recruit.jobPostTitle}
					</a>
				</h3>
                <div class="job-details">
                    <span>📍 ${recruit.location}</span>
                    <span class="divider">|</span>
                    <span>💼 ${employmentTypeText}</span>
                    <span class="divider">|</span>
                    <span>📈 ${experienceText}</span>
                    <span class="divider">|</span>
                    <span>💰 ${salaryText}</span>
                </div>
            </div>

            <div class="apply-info-wrapper">
                <a href="/recruit/view/${recruit.jobPostId}" target="_blank" class="apply-btn">입사지원</a>
                <div class="date-info reg-date">📅 등록일: ${recruit.createAt}</div>
                <div class="date-info deadline-date">${deadlineDate}</div>
            </div>
        </div>
    `;
}

function performSearch() {

	currentFilters.keyword = $('#main-search').val().trim();

	// 키워드를 제외한 다른 필터가 활성화되어 있는지 확인
	if (hasActiveFilters()) {
		// 다른 필터가 있으면 키워드를 필터 조건에 추가만 하고, 실제 검색은 '적용' 버튼
		renderSelectedFilterTags();

	} else {
		// 다른 필터가 없으면 즉시 검색을 실행
		currentFilters.page = 1;
		loadRecruitList(currentFilters);
		renderSelectedFilterTags(); // 검색어 태그를 표시하기 위해 호출
	}
}

function performRegionSearch() {

	currentFilters.regionKeyword = $('.filter-search').val().trim();
	renderSelectedFilterTags();
}

// 선택된 필터들을 화면에 그려주는 함수
function renderSelectedFilterTags() {

	const tagsWrapper = $('#selected-tags-wrapper');
	tagsWrapper.empty();

	let hasActiveFilters = false;

	// 태그를 생성하는 함수
	const createTag = (type, value, label) => {

		const tagHTML = `
			<div class="filter-tag" data-type="${type}" data-value="${value}">
                <span>${label}</span>
                <button type="button" class="remove-tag">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                    </svg>
                </button>
            </div>`;
		tagsWrapper.append(tagHTML);
		hasActiveFilters = true;
	};

	// 키워드
	if (currentFilters.keyword) {
		createTag('keyword', currentFilters.keyword, `검색어: ${currentFilters.keyword}`);
	}
	// 지역 검색어
	if (currentFilters.regionKeyword) {
		createTag('regionKeyword', currentFilters.regionKeyword, `지역 검색어: ${currentFilters.regionKeyword}`);
	}
	// 지역
	if (currentFilters.region) {
		createTag('region', currentFilters.region, `지역: ${currentFilters.region}`);
	}
	// 기술 스택
	currentFilters.techStacks.forEach(stackId => {
		const stackName = $(`#stack-options .filter-btn[data-value="${stackId}"]`).text();
		if (stackName) createTag('techStacks', stackId, `기술: ${stackName}`);
	});
	// 고용 형태
	currentFilters.employmentTypes.forEach(type => {
		const typeName = $(`#detail-dropdown .filter-btn[data-value="${type}"]`).text();
		if (typeName) createTag('employmentTypes', type, typeName);
	});
	// 경력
	currentFilters.experienceTypes.forEach(type => {
		const typeName = $(`#detail-dropdown .filter-btn[data-value="${type}"]`).text();
		if (typeName) createTag('experienceTypes', type, typeName);
	});
	// 연봉 정보만 보기
	if (currentFilters.salaryOnly) {
		createTag('salaryOnly', 'true', '연봉 정보가 있는 공고만');
	}

	// 활성화된 필터 유무 & 태그 변화에 따라 영역을 보여주거나 숨김
	if (hasActiveFilters || areFiltersChanged()) {
		$('#selected-filters-area').show();
	} else {
		$('#selected-filters-area').hide();
	}
}

function hasActiveFilters() {
	return currentFilters.region ||
		currentFilters.techStacks.length > 0 ||
		currentFilters.employmentTypes.length > 0 ||
		currentFilters.experienceTypes.length > 0 ||
		currentFilters.salaryOnly ||
		currentFilters.regionKeyword;
}

// 현재 필터와 적용된 필터가 다른지 비교
function areFiltersChanged() {
	return JSON.stringify(currentFilters) !== JSON.stringify(appliedFilters);
}

// 필터 변경 여부에 따라 '적용' 버튼의 활성/비활성 상태를 업데이트
function updateApplyButtonState() {
	const applyBtn = $('#apply-filters-btn');
	if (areFiltersChanged()) {
		// 변경점이 있으면: 버튼 활성화 및 문구 변경
		applyBtn.prop('disabled', false).text('선택한 조건으로 검색');
	} else {
		// 변경점이 없으면: 버튼 비활성화 및 문구 변경
		applyBtn.prop('disabled', true).text('검색완료');
	}
}
