// /js/applicant/mypage/inquiries.js

$(document).ready(function () {
    let currentPage = 0;
    const pageSize = 10;

    // 초기 로딩
    loadInquiries(currentPage, pageSize);

    // 검색 버튼 클릭
    $("#searchBtn").on("click", function () {
        currentPage = 0;
        loadInquiries(currentPage, pageSize);
    });

    // 초기화 버튼 클릭
    $("#resetBtn").on("click", function () {
        $("#searchKeyword").val("");
        $("#sort-order").val("latest");
        currentPage = 0;
        loadInquiries(currentPage, pageSize);
    });

    // 데이터 로딩 함수
    function loadInquiries(page, size) {
        const keyword = $("#searchKeyword").val();
        const sort = $("#sort-order").val();

        $.ajax({
            url: "/api/users/inquiries",
            method: "GET",
            data: {
                page: page,
                size: size,
                keyword: keyword,
                sort: sort
            },
            success: function (response) {
                renderTable(response.content);
                renderPagination(response.totalPages, response.number + 1, function (selectedPage) {
                    currentPage = selectedPage - 1;
                    loadInquiries(currentPage, pageSize);
                });
            },
            error: function () {
                alert("문의 목록을 불러오는 데 실패했습니다.");
            }
        });
    }

    // 테이블 렌더링
    function renderTable(inquiries) {
        const $tbody = $(".request-table tbody");
        $tbody.empty();

        if (inquiries.length === 0) {
            $tbody.append(`<tr><td colspan="6">문의 내역이 없습니다.</td></tr>`);
            return;
        }

        inquiries.forEach(inquiry => {
            const createdDate = formatDate(inquiry.createdAt);
            const repliedDate = inquiry.repliedAt ? formatDate(inquiry.repliedAt) : "-";
            const status = inquiry.status === "ANSWERED" ? "답변완료" : "답변대기";

            $tbody.append(`
                <tr>
                    <td>${inquiry.id}</td>
                    <td><a href="/request/${inquiry.id}">${inquiry.reasonName || "-"}</a></td>
                    <td><a href="/request/${inquiry.id}">${inquiry.title}</a></td>
                    <td>${createdDate}</td>
                    <td>${status}</td>
                    <td>${repliedDate}</td>
                </tr>
            `);
        });
    }

    // 날짜 포맷
    function formatDate(dateTimeStr) {
        const date = new Date(dateTimeStr);
        return date.toISOString().split("T")[0];
    }
});

$("#inquiryBtn").on("click", function () {
    window.location.href = "/support/inquiry";
});
