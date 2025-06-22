$(document).on("click", ".toggle-details-btn", function () {
    const userId = $(this).data("id");
    const $wrapper = $(".row-" + userId).find(".detail-table-wrapper");

    // 다른 열린 상세정보 닫기
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
