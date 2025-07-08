$(document).ready(function () {
    // --------- 플로팅바 닫기 관련 ---------
    $(document).on('click', '.close-button', function () {
        closeFloatingSidebar();
    });

    $(document).on('keydown', function (e) {
        if (e.key === "Escape") {
            closeFloatingSidebar();
        }
    });

    $(document).on('dblclick', '#floatingOverlay', function () {
        closeFloatingSidebar();
    });

    function closeFloatingSidebar() {
        $('#floatingOverlay').removeClass('show');
        $('#floatingSidebar').removeClass('show');
        $('#floatingSidebarContent').empty();
    }

    // --------- 수정 버튼 클릭 시 ---------
    $(document).on('click', '.edit-button', function () {
        const jobPostId = $(this).data('id');

        $.ajax({
            url: `/employer/jobpost_edit?jobPostId=${jobPostId}`,
            method: 'GET',
            success: function (html) {
                $('#floatingSidebarContent').html(html);
                $('#floatingOverlay').addClass('show');
                $('#floatingSidebar').addClass('show');
            },
            error: function () {
                alert('수정 페이지를 불러오지 못했습니다.');
            }
        });
    });
// --------- 삭제 버튼 클릭 시 ---------
    $(document).on('click', '.delete-button', function () {
        const jobPostId = $(this).data('id');
        if (!confirm("선택한 공고를 삭제하시겠습니까?")) return;

        $.ajax({
            url: '/employer/jobpost_delete',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify([jobPostId]),
            success: function () {
                alert("삭제가 완료되었습니다.");
                location.reload();
            },
            error: function () {
                alert("삭제 중 오류가 발생했습니다.");
            }
        });
    });
    // --------- 지도 표시 로직 추가 ---------
    const address = $('[data-field="workplace_address"]').text().trim();

    if (window.kakao && kakao.maps && address) {
        const mapContainer = document.getElementById('workplaceMap');
        const mapOption = {
            center: new kakao.maps.LatLng(37.5665, 126.9780), // 기본 위치
            level: 3
        };

        const map = new kakao.maps.Map(mapContainer, mapOption);

        const geocoder = new kakao.maps.services.Geocoder();
        geocoder.addressSearch(address, function (result, status) {
            if (status === kakao.maps.services.Status.OK) {
                const coords = new kakao.maps.LatLng(result[0].y, result[0].x);

                new kakao.maps.Marker({
                    map: map,
                    position: coords
                });

                map.setCenter(coords);
            } else {
                console.warn('주소 검색 실패:', status);
            }
        });
    }
});
