// /static/js/admin/dashboard.js

document.addEventListener('DOMContentLoaded', () => {
    const data = window.chartData;

    renderLineChart('dailyUserChart', data.dailyUserCount, '가입자 수');
    renderLineChart('dailyLoginChart', data.dailyLoginCount, '로그인 수');
    renderPieChart('userRoleChart', data.userRoleDistribution, '회원 유형 비율');
    renderPieChart('loginTypeChart', data.loginTypeDistribution, '소셜 로그인 비율');
    renderLineChart('dailyResumeChart', data.dailyResumeCount, '이력서 등록 수');

    const resumeStats = data.resumeCompletionStats;
    renderDoughnutChart('resumeCompletionChart', {
        '완료': resumeStats.completedResumes,
        '미완료': resumeStats.totalApplicants - resumeStats.completedResumes
    }, '이력서 작성 완료율');

    renderBarChart('resumeJobChart', data.resumeJobDistribution, '직무별 이력서');
    renderLineChart('dailyJobPostChart', data.dailyJobPostCount, '공고 등록 수');
    renderBarChart('jobPostCategoryChart', data.jobPostCategoryDistribution, '직무별 공고');
});

// ============================
// ✅ 유틸 함수
// ============================

function renderLineChart(id, rawData, label) {
    const ctx = document.getElementById(id)?.getContext('2d');
    if (!ctx || !rawData) return;

    // 최근 5일 날짜 생성
    const today = new Date();
    const labels = Array.from({ length: 5 }, (_, i) => {
        const date = new Date(today);
        date.setDate(today.getDate() - (4 - i));
        return date.toISOString().split('T')[0];
    });

    // rawData를 날짜 기준으로 매핑
    const dataMap = {};
    rawData.forEach(item => {
        dataMap[item.date] = item.count;
    });

    // 값이 없으면 0으로 채움
    const values = labels.map(date => dataMap[date] || 0);

    new Chart(ctx, {
        type: 'line',
        data: {
            labels,
            datasets: [{
                label,
                data: values,
                borderColor: 'rgb(75, 192, 192)',
                borderWidth: 2,
                tension: 0.3,
                pointRadius: 5,
                pointHoverRadius: 7,
                pointBackgroundColor: 'rgb(75, 192, 192)',
                pointBorderColor: '#ffffff',
                pointBorderWidth: 2
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
                    enabled: true,
                    callbacks: {
                        label: function (context) {
                            return `${label}: ${context.parsed.y}`;
                        }
                    }
                }
            }

        }
    });
}

function renderPieChart(id, rawData, label) {
    const ctx = document.getElementById(id)?.getContext('2d');
    if (!ctx || !rawData) return;

    const labels = rawData.map(item => item.role || item.login_type);
    const values = rawData.map(item => item.count);

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
    if (!ctx || !dataMap) return;

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

    const labels = rawData.map(item => item.job_category);
    const values = rawData.map(item => item.count);

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
    const colors = [
        '#4dc9f6', '#FFA046', '#23e377', '#537bc4',
        '#f67019', '#166a8f', '#f53794', '#58595b',
        '#8549ba'
    ];
    return Array.from({ length: count }, (_, i) => colors[i % colors.length]);
}
