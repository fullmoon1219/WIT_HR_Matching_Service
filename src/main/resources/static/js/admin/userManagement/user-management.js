// 경고 수에 따라 클래스 지정
function getWarningClass(count) {
    if (count >= 3) return 'warning-3';
    else if (count === 2) return 'warning-2';
    else if (count === 1) return 'warning-1';
    return 'warning-0';
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

    // 단일 사용자 변경
    $("#userTableBody").on("click", ".user-specific .value", function (e) {
        e.stopPropagation();
        const popup = $(this).siblings(".value-popup");
        $(".value-popup").not(popup).hide();
        popup.toggle();
    });

    // 단일 사용자 변경 - 버튼 클릭
    $("#userTableBody").on("click", ".user-specific .value-popup button", function () {
        const newValue = $(this).data("value");
        const wrapper = $(this).closest(".value-wrapper");
        const span = wrapper.find(".value");
        const userId = parseInt(span.data("id"));
        const type = span.data("type");

        const urlMap = {
            role: "/api/admin/users/role",
            status: "/api/admin/users/status",
            warning: "/api/admin/users/warning"
        };

        const dataMap = {
            role: { userIds: [userId], role: newValue },
            status: { userIds: [userId], status: newValue },
            warning: { userIds: [userId], count: parseInt(newValue) }
        };

        $.ajax({
            type: "PATCH",
            url: urlMap[type],
            contentType: "application/json",
            data: JSON.stringify(dataMap[type]),
            success: function () {
                location.reload();
            },
            error: function () {
                alert(`${type.toUpperCase()} 변경 실패`);
            }
        });
    });

    // ✅ 일괄 변경 말풍선 토글
    $(".bulk-action .bulk-value").on("click", function (e) {
        e.stopPropagation();
        const popup = $(this).siblings(".value-popup");
        $(".value-popup").not(popup).hide();
        popup.toggle();
    });

    // ✅ 일괄 변경 처리
    $(".bulk-action .value-popup button").on("click", function () {
        const newValue = $(this).data("value");
        const wrapper = $(this).closest(".value-wrapper");
        const span = wrapper.find(".bulk-value");
        const type = span.data("type");

        const userIds = $('.user-checkbox:checked').map(function () {
            return parseInt($(this).val());
        }).get();

        if (!newValue || userIds.length === 0) {
            $(".value-popup").hide();
            return;
        }

        const urlMap = {
            role: "/api/admin/users/role",
            status: "/api/admin/users/status",
            warning: "/api/admin/users/warning"
        };

        const dataMap = {
            role: { userIds: userIds, role: newValue },
            status: { userIds: userIds, status: newValue },
            warning: { userIds: userIds, count: parseInt(newValue) }
        };

        $.ajax({
            type: "PATCH",
            url: urlMap[type],
            contentType: "application/json",
            data: JSON.stringify(dataMap[type]),
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

    // 페이지네이션 버튼 클릭 시
    $(document).on('click', '.pagination-btn', function () {
        const selectedPage = $(this).data('page');
        loadUserList(selectedPage);
    });
});
