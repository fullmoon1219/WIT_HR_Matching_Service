// 경고 수에 따라 클래스 지정
function getWarningClass(count) {
    if (count >= 3) return 'warning-3';
    else if (count === 2) return 'warning-2';
    else if (count === 1) return 'warning-1';
    return 'warning-0';
}

$(document).on("click", "td.selectable", function (e) {
    // 체크박스를 직접 클릭한 경우는 무시
    if ($(e.target).is("input[type='checkbox']")) return;

    const checkbox = $(this).find("input[type='checkbox']");
    checkbox.prop("checked", !checkbox.prop("checked"));
});


// 전체 선택/해제
$(document).on("change", "#checkAll", function () {
    $(".user-checkbox").prop("checked", this.checked);
});

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

    $("#userTableBody").on("click", ".user-specific .value", function (e) {
        e.stopPropagation();

        const popup = $(this).siblings(".value-popup");
        $(".value-popup").not(popup).hide(); // 다른 말풍선 닫기
        popup.toggle(); // 현재 토글
    });

    $("#userTableBody").on("click", ".user-specific .value-popup button", function () {
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

    // ✅ 일괄 변경용 말풍선 토글
    $(".bulk-action .bulk-value").on("click", function (e) {
        e.stopPropagation();

        const popup = $(this).siblings(".value-popup");
        $(".value-popup").not(popup).hide();
        popup.toggle();
    });

    // ✅ 일괄 변경용 버튼 클릭 시 처리
    $(".bulk-action .value-popup button").on("click", function () {
        const newValue = $(this).data("value");
        const wrapper = $(this).closest(".value-wrapper");
        const span = wrapper.find(".bulk-value");
        const type = span.data("type");

        const userIds = $('.user-checkbox:checked').map(function () {
            return $(this).val();
        }).get();

        if (newValue === null || newValue === "" || userIds.length === 0) {
            $(".value-popup").hide();
            return;
        }

        $.ajax({
            type: "POST",
            url: "/api/admin/users/change-value",
            data: {
                userIds: userIds, // 복수 사용자 ID
                type: type,
                value: newValue
            },
            traditional: true, // 배열 직렬화 옵션 (userIds=1&userIds=2)
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


