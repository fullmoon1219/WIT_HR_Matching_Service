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

                // 지도가 있는 경우 지도 로딩
                if (typeof afterAjaxLoaded === 'function') {
                    afterAjaxLoaded();
                }
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
});

// --------- 지도 초기화 함수 ---------
function initWorkplaceMap() {
    const address = document.querySelector('[data-field="workplace_address"]')?.textContent.trim();

    if (!window.kakao || !kakao.maps || !address) {
        console.warn('지도 또는 주소 정보를 불러올 수 없습니다.');
        return;
    }

    const mapContainer = document.getElementById('workplaceMap');
    if (!mapContainer) {
        console.warn("지도 컨테이너가 존재하지 않습니다.");
        return;
    }

    const mapOption = {
        center: new kakao.maps.LatLng(37.5665, 126.9780), // 기본 위치 (서울)
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
            console.warn('주소 좌표 변환 실패:', status);
        }
    });
}

// --------- Ajax 로드 후 지도 실행용 함수 (외부에서 호출) ---------
function afterAjaxLoaded() {
    if (window.kakao && kakao.maps && kakao.maps.load) {
        kakao.maps.load(function () {
            initWorkplaceMap();
        });
    } else {
        console.error("Kakao 지도 API가 로드되지 않았습니다.");
    }
}
