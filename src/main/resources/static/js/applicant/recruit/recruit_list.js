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

$(document).ready(function () {
	$('.filter-dropdown').hide();

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
		if ($(e.target).is('a, a *')) return;

		const jobPostId = $(this).data('jobpost-id');
		if (jobPostId) window.open(`/recruit/view/${jobPostId}`, '_blank');
	});

	$('#pagination').on('click', '.page-btn', function(e) {
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
			page: 1,
			sortOrder: 'latest',
			employmentTypes: [],
			experienceTypes: [],
			salaryOnly: false
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
			clickedButton.addClass('active').siblings().removeClass('active');
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
				recruits.forEach(function(recruit) {
					const row = makeRow(recruit);
					tbody.append(row);
				});
			}

			renderPagination(pagingInfo);
			$('#totalRecord').html(pagingInfo.totalRecord);
			totalPage = pagingInfo.totalPage;
			$('#total-page-info').text(` / 총 ${totalPage}페이지`);
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
	return `
		<div class="recruit-card" data-jobpost-id="${recruit.jobPostId}">
			<div class="card-title">
				<h3>${recruit.jobPostTitle}</h3>
				<div class="company-name-container">${recruit.companyName}</div>
			</div>
			<div class="work-location">${recruit.location}</div>
			<div class="work-time">${translateEmploymentType(recruit.employmentType)}</div>
			<div class="salary">${formatSalary(recruit.salary)}</div>
			<div class="reg-date">${recruit.createAt}</div>
		</div>
	`;
}

function renderPagination(pagingInfo) {
	const pagination = $('#pagination');
	pagination.empty();

	const currentPage = pagingInfo.currentPage || pagingInfo.page;  // 둘 다 대응
	const totalPage = pagingInfo.totalPage;

	for (let i = 1; i <= totalPage; i++) {
		const $btn = $('<a>')
			.attr('href', '#')
			.addClass('page-btn')
			.attr('data-page', i)
			.text(i);

		if (i === currentPage) {
			$btn.addClass('active');  // ⭐ active 클래스를 여기서 정확히 추가
		}

		pagination.append($btn);
	}
}


function formatSalary(salary) {
	if (!salary || String(salary).trim() === '') return '';
	if (!isNaN(parseFloat(salary)) && isFinite(salary)) return `💰 ${salary}만원`;
	return `💰 ${salary}`;
}

function performSearch() {
	const keyword = $('#main-search').val();
	currentFilters = {
		...currentFilters,
		keyword,
		page: 1,
		sortOrder: 'latest',
		techStacks: [],
		region: '',
		regionKeyword: '',
		employmentTypes: [],
		experienceTypes: [],
		salaryOnly: false
	};
	$('.filter-options .filter-btn').removeClass('active');
	$('.filter-toggle').removeClass('active');
	$('.filter-dropdown').hide();
	$('#sort-order-select').val('latest');
	loadRecruitList(currentFilters);
}

function performRegionSearch() {
	const regionKeyword = $('.filter-search').val();
	currentFilters = {
		...currentFilters,
		regionKeyword,
		keyword: '',
		page: 1,
		sortOrder: 'latest',
		region: ''
	};
	$('#region-options .filter-btn').removeClass('active');
	$('#sort-order-select').val('latest');
	loadRecruitList(currentFilters);
}
