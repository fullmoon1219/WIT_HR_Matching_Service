// /js/support/inquiry-answer.js

$(document).ready(function () {
    // URL에서 inquiryId 추출 (예: /support/inquiry/15)
    const pathParts = window.location.pathname.split('/');
    const inquiryId = pathParts[pathParts.length - 1];

    if (!inquiryId) {
        console.error("문의 ID가 없습니다.");
        return;
    }

    // 상세 조회 API 호출
    $.ajax({
        url: `/api/users/inquiries/${inquiryId}`,
        method: 'GET',
        success: function (inquiry) {
            renderInquiryDetail(inquiry);
        },
        error: function (xhr) {
            console.error("문의 상세 조회 실패:", xhr);
            alert("문의 정보를 불러오는 데 실패했습니다.");
        }
    });

    // 렌더링 함수
    function renderInquiryDetail(inquiry) {
        // 제목 세팅
        $(".section-title").text("Q. " + inquiry.title);

        // 기본 정보 테이블 세팅
        const nameWithEmail = `${inquiry.name} (${inquiry.email})`;
        const createdDate = formatDate(inquiry.createdAt);
        const statusText = inquiry.status === 'ANSWERED' ? '완료' : '미답변';

        $(".info-table").html(`
            <tr>
                <th>이름</th>
                <td>${nameWithEmail}</td>
                <th>등록일</th>
                <td>${createdDate}</td>
            </tr>
            <tr>
                <th>답변여부</th>
                <td colspan="3">${statusText}</td>
            </tr>
        `);

        // 문의 내용 출력
        $(".content-text").text(inquiry.content);

        // 답변 출력
        if (inquiry.status === 'ANSWERED' && inquiry.reply) {
            $(".qa-label").show();
            $(".qa-content").text(inquiry.reply);
            $(".reply-time").text("답변시간: " + formatDateTime(inquiry.repliedAt));
        } else {
            // 답변 없을 경우 숨김 처리
            $(".qa-section").hide();
        }
    }

    // 날짜 포맷 함수 (YYYY/MM/DD)
    function formatDate(dateTimeStr) {
        if (!dateTimeStr) return '';
        const date = new Date(dateTimeStr);
        return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`;
    }

    // 날짜+시간 포맷 함수 (YYYY/MM/DD HH:mm:ss)
    function formatDateTime(dateTimeStr) {
        if (!dateTimeStr) return '';
        const date = new Date(dateTimeStr);
        return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
    }

    // 두 자리수 보장
    function pad(n) {
        return n < 10 ? '0' + n : n;
    }
});
