$(document).ready(function () {
    loadRandomJobs(0, 3);      // 초기 로딩: 랜덤 공고
    loadUrgentJobs(0, 8, 5);   // 초기 로딩: 마감 임박 공고 (5일 이내)
});

/**
 * 랜덤 공고 로드
 */
function loadRandomJobs(page, count) {
    $.ajax({
        url: "/api/jobs/random",
        method: "GET",
        data: { page: page, count: count },
        success: function (response) {
            renderRandomJobs(response.content);

            // 페이지네이션 표시
            renderPagination(response.totalPages, response.number + 1, (selectedPage) => {
                loadRandomJobs(selectedPage - 1, count);
            });
        },
        error: function () {
            console.error("랜덤 공고 로딩 실패");
        }
    });
}

function renderRandomJobs(jobList) {
    const $container = $(".card-grid");
    $container.empty();

    // 배너 이미지 유지
    $container.append(`
        <div class="image-card">
            <img src="/images/today-banner.png" alt="합격축하금 배너">
        </div>
    `);

    jobList.forEach(job => {
        const dday = calculateDday(job.deadline);
        const card = `
            <a href="/recruit/view/${job.id}" class="job-card-link">
                <div class="job-card">
                    <div class="company">${job.companyName || "알 수 없음"}</div>
                    <h3>${job.title}</h3>
                    <p>${job.salary || ""}</p>
                    <div class="dday">D-${dday}</div>
                </div>
            </a>
        `;
        $container.append(card);
    });
}

/**
 * 마감 임박 공고 로드
 */
function loadUrgentJobs(page, size, days) {
    $.ajax({
        url: "/api/jobs/urgent",
        method: "GET",
        data: { page: page, size: size, days: days },
        success: function (response) {
            renderUrgentJobs(response.content);

            // 페이지네이션 표시
            renderPagination(response.totalPages, response.number + 1, (selectedPage) => {
                loadUrgentJobs(selectedPage - 1, size, days);
            });
        },
        error: function () {
            console.error("마감 임박 공고 로딩 실패");
        }
    });
}

function renderUrgentJobs(jobList) {
    const $container = $(".urgent-card-grid");
    $container.empty();

    jobList.forEach(job => {
        const dday = calculateDday(job.deadline);
        const card = `
            <a href="/recruit/view/${job.id}" class="job-card-link">
                <div class="job-card">
                    <div class="company">${job.companyName || "알 수 없음"}</div>
                    <h3>${job.title}</h3>
                    <p>${job.salary || ""}</p>
                    <div class="dday">D-${dday}</div>
                </div>
            </a>
        `;
        $container.append(card);
    });
}

/**
 * D-day 계산 함수
 */
function calculateDday(deadline) {
    const now = new Date();
    const end = new Date(deadline);
    const diff = Math.ceil((end - now) / (1000 * 60 * 60 * 24));
    return diff >= 0 ? diff : 0;
}
