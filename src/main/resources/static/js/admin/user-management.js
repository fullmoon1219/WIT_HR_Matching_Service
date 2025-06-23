// 경고 수에 따라 클래스 지정
function getWarningClass(count) {
    if (count >= 3) return 'warning-3';
    else if (count === 2) return 'warning-2';
    else if (count === 1) return 'warning-1';
    return 'warning-0';
}

// 전체 선택/해제
$(document).on("change", "#checkAll", function () {
    $(".user-checkbox").prop("checked", this.checked);
});

// 선택된 유저 ID 목록
function getSelectedUserIds() {
    return $(".user-checkbox:checked").map(function () {
        return $(this).val();
    }).get();
}

// 로그인 타입 표시
function renderLoginType(type) {
    switch (type) {
        case 'EMAIL':
            return '<span class="login-type email"><i class="fa-solid fa-envelope"></i> 이메일</span>';
        case 'NAVER':
            return '<span class="login-type naver"><i class="fa-brands fa-naver"></i> 네이버</span>';
        case 'GOOGLE':
            return '<span class="login-type google"><i class="fa-brands fa-google"></i> 구글</span>';
        default:
            return `<span class="login-type unknown">${type}</span>`;
    }
}

// 상세정보 토글 버튼 클릭
$(document).on("click", ".toggle-details-btn", function () {
    const userId = $(this).data("id");
    const $wrapper = $(".row-" + userId).find(".detail-table-wrapper");

    $(".detail-table-wrapper").not($wrapper).slideUp();
    $(".toggle-details-btn").not(this).text("▼");

    if ($wrapper.is(":visible")) {
        $wrapper.slideUp();
        $(this).text("▼");
    } else {
        $wrapper.slideDown();
        $(this).text("▲");
    }
});

$(document).ready(function () {

    // ⬇️ 이벤트 위임: 동적으로 생긴 .value 클릭 처리
    $("#userTableBody").on("click", ".value", function (e) {
        e.stopPropagation();

        const popup = $(this).siblings(".value-popup");
        $(".value-popup").not(popup).hide(); // 다른 말풍선 닫기
        popup.toggle(); // 현재 토글
    });

    // ⬇️ 이벤트 위임: 팝업 버튼 클릭 처리
    $("#userTableBody").on("click", ".value-popup button", function () {
        const newValue = $(this).data("value");
        const wrapper = $(this).closest(".value-wrapper");
        const span = wrapper.find(".value");
        const userId = span.data("id");
        const type = span.data("type");

        $.ajax({
            type: "POST",
            url: "/api/admin/users/change-value",
            data: { userId: userId, type: type, value: newValue },
            success: function () {
                location.reload();
            },
            error: function () {
                alert(`${type.toUpperCase()} 변경 실패`);
            }
        });
    });

    // 외부 클릭 시 말풍선 닫기
    $(document).on("click", function () {
        $(".value-popup").hide();
    });
});


