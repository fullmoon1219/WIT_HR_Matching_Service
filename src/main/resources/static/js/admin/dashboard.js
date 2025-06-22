//  /static/js/admin/dashboard.js

$(document).ready(function () {
    $.get("/api/admin/dashboard/daily-user-counts", function (data) {
        renderLineChart("dailyUserChart", data, "일일 가입자 수");
    });

    $.get("/api/admin/dashboard/daily-login-counts", function (data) {
        renderLineChart('dailyLoginChart', data, '일일 로그인 수');
    });

    $.get("/api/admin/dashboard/user-role-distribution", function (data) {
        renderPieChart('userRoleChart', data, '회원 유형 비율');
    });

    $.get("/api/admin/dashboard/login-type-distribution", function (data) {
        renderPieChart('loginTypeChart', data, '소셜 로그인 비율');
    });

    $.get("/api/admin/dashboard/daily-resume-counts", function (data) {
        renderLineChart('dailyResumeChart', data, '이력서 등록 수');
    });

    $.get("/api/admin/dashboard/resume-completion-stats", function (data) {
        renderDoughnutChart('resumeCompletionChart', {
            '완료': data.completedResumes,
            '미완료': data.totalApplicants - data.completedResumes
        }, '이력서 작성 완료율');
    });

    $.get("/api/admin/dashboard/resume-job-distribution", function (data) {
        renderBarChart('resumeJobChart', data, '직무별 이력서');
    });

    $.get("/api/admin/dashboard/daily-jobpost-counts", function (data) {
        renderLineChart('dailyJobPostChart', data, '공고 등록 수');
    });

    $.get("/api/admin/dashboard/job-post-category-distribution", function (data) {
        renderBarChart('jobPostCategoryChart', data, '직무별 공고');
    });
});

// 전역에서 모든 차트 인스턴스를 저장
const chartInstances = {};

function renderLineChart(id, rawData, label) {
    const canvas = document.getElementById(id);
    if (!canvas || !rawData) return;

    const ctx = canvas.getContext('2d');

    // 기존 차트가 있으면 파괴
    if (chartInstances[id]) {
        chartInstances[id].destroy();
    }

    // 날짜 라벨 5일치 생성 (서버에서 반환하는 key 형식 'MM-DD'에 맞춤)
    const today = new Date();
    const labels = Array.from({ length: 5 }, (_, i) => {
        const d = new Date(today);
        d.setDate(today.getDate() - (4 - i));
        return d.toISOString().slice(5, 10); // 'MM-DD' 형식
    });

    // 데이터 매핑 (없는 날짜는 0으로)
    const values = labels.map(date => Number(rawData[date]) || 0);

    // 새 차트 생성
    const chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: label,
                data: values,
                borderColor: 'rgb(75, 192, 192)',
                borderWidth: 2,
                tension: 0.3,
                pointRadius: 5,
                pointHoverRadius: 7,
                pointBackgroundColor: 'rgb(75, 192, 192)',
                pointBorderColor: '#ffffff',
                pointBorderWidth: 2,
                fill: false
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                mode: 'nearest',
                intersect: false
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        precision: 0,
                        stepSize: 1
                    }
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            return `${label}: ${context.parsed.y}`;
                        }
                    }
                }
            }
        }
    });

    // 저장
    chartInstances[id] = chart;
}





function renderPieChart(id, rawData, label) {
    const ctx = document.getElementById(id)?.getContext('2d');
    if (!ctx || !rawData) return;

    // 객체를 키-값 쌍으로 나눠서 처리
    const labels = Object.keys(rawData);
    const values = Object.values(rawData);

    new Chart(ctx, {
        type: 'pie',
        data: {
            labels,
            datasets: [{
                label,
                data: values,
                backgroundColor: generateColors(values.length)
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
}


function renderDoughnutChart(id, dataMap, label) {
    const ctx = document.getElementById(id)?.getContext('2d');
    if (!ctx || !dataMap || typeof dataMap !== 'object') return;

    const labels = Object.keys(dataMap);
    const values = Object.values(dataMap);

    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels,
            datasets: [{
                label,
                data: values,
                backgroundColor: generateColors(values.length)
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
}


function renderBarChart(id, rawData, label) {
    const ctx = document.getElementById(id)?.getContext('2d');
    if (!ctx || !rawData) return;

    // 객체 → 배열
    const labels = Object.keys(rawData);
    const values = Object.values(rawData);

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels,
            datasets: [{
                label,
                data: values,
                backgroundColor: generateColors(values.length)
            }]
        },
        options: {
            responsive: true,
            indexAxis: 'y',
            maintainAspectRatio: false,
            scales: {
                x: {
                    beginAtZero: true,
                    ticks: {
                        precision: 0,
                        stepSize: 1
                    }
                }
            }
        }
    });
}


function generateColors(count) {
    const baseColors = [
        '#4dc9f6', '#FFA046', '#23e377', '#537bc4',
        '#f67019', '#166a8f', '#f53794', '#58595b',
        '#8549ba'
    ];
    return Array.from({ length: count }, (_, i) => baseColors[i % baseColors.length]);
}
