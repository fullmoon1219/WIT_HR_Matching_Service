// /js/admin/admin-template.js

function openFloatingSidebar(contentHtml) {
    $("#floatingSidebarContent").html(contentHtml);
    $("#floatingSidebar").addClass("show");
}

function closeFloatingSidebar() {
    $("#floatingSidebar").removeClass("show");
}

// 닫기 버튼 클릭 시 닫기
$("#closeFloatingSidebar").on("click", function () {
    closeFloatingSidebar();
});

// 플로팅 바 외부 클릭 시 닫기 (버튼 같은 요소 클릭 시 무시)
$(document).on("mousedown", function (e) {
    const $sidebar = $("#floatingSidebar");
    const $content = $(".floating-sidebar-inner");

    // 플로팅바가 열려있고, 클릭한 위치가 플로팅바 내부가 아니며, 클릭한 요소가 버튼도 아닐 때
    if (
        $sidebar.hasClass("show") &&
        !$content.is(e.target) &&
        $content.has(e.target).length === 0 &&
        !$(e.target).is("button") &&
        !$(e.target).closest("button").length &&
        !$(e.target).is("a") &&
        !$(e.target).closest("a").length &&
        !$(e.target).is("input") &&
        !$(e.target).closest("input").length
    ) {
        closeFloatingSidebar();
    }
});

