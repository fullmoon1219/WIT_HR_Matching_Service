$(document).on("click", "td.selectable", function (e) {
    // placeholder 제외
    if ($(this).hasClass("placeholder")) return;

    // 체크박스를 직접 클릭한 경우는 무시
    if ($(e.target).is("input[type='checkbox']")) return;

    const checkbox = $(this).find("input[type='checkbox']");
    checkbox.prop("checked", !checkbox.prop("checked"));
});


// 전체 선택/해제 (disabled 제외)
$(document).on("change", "#checkAll", function () {
    $(".user-checkbox:not(:disabled)").prop("checked", this.checked);
});

// 개별 체크박스 상태 변경 시 "전체 선택" 체크 상태도 조정
$(document).on("change", ".user-checkbox:not(:disabled)", function () {
    const all = $(".user-checkbox:not(:disabled)");
    const checked = $(".user-checkbox:not(:disabled):checked");

    $("#checkAll").prop("checked", all.length === checked.length);
});