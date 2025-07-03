// // /js/employer/post-write.js
//
// $(document).ready(function () {
//     let isClosing = false; // 중복 방지용 플래그
//
//     function isFormModified() {
//         const inputChanged = $('#floatingSidebarContent')
//             .find('input[type="text"], input[type="date"], textarea')
//             .toArray()
//             .some(el => el.value.trim() !== (el.defaultValue?.trim?.() || ''));
//
//         if (inputChanged) return true;
//
//         const selectChanged = $('#floatingSidebarContent')
//             .find('select')
//             .toArray()
//             .some(select => {
//                 let initialValue = '';
//                 $(select).find('option').each(function () {
//                     if (this.defaultSelected) {
//                         initialValue = this.value;
//                         return false;
//                     }
//                 });
//                 return $(select).val() !== initialValue;
//             });
//
//         return selectChanged;
//     }
//
//     function attemptCloseFloatingSidebar(e) {
//         if (isClosing) return; // 중복 실행 방지
//         isClosing = true;
//
//         if (e) {
//             e.preventDefault();
//             e.stopPropagation();
//         }
//
//         if (isFormModified()) {
//             if (!confirm('작성 중인 내용을 취소하시겠습니까?')) {
//                 isClosing = false; // 취소 시 플래그 초기화
//                 return;
//             }
//         }
//
//         $('#floatingOverlay').removeClass('show');
//         $('#floatingSidebar').removeClass('show');
//         $('#floatingSidebarContent').empty();
//
//         setTimeout(() => {
//             isClosing = false; // 완료 후 플래그 해제
//         }, 200); // 200ms 후 플래그 해제 (이벤트 중복 대비)
//     }
//
//     // 취소 버튼
//     $(document).on('click', '.cancel-button', function (e) {
//         attemptCloseFloatingSidebar(e);
//     });
//
//     // ESC 키 (한 번만 반응하도록 keyup 사용)
//     $(document).on('keyup', function (e) {
//         if (e.key === "Escape") {
//             attemptCloseFloatingSidebar(e);
//         }
//     });
//
//     // 오버레이 더블클릭
//     $(document).on('dblclick', '#floatingOverlay', function (e) {
//         attemptCloseFloatingSidebar(e);
//     });
// });

$(document).ready(function () {
    let isClosing = false; // 중복 방지용 플래그

    // -------- 기술 스택 선택 기능 시작 --------
    const selectedStacks = new Set();

    // 스택 버튼 클릭 시 선택/해제
    $(document).on('click', '.stack-tag', function () {
        const value = $(this).data('value');

        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            selectedStacks.delete(value);
        } else {
            $(this).addClass('selected');
            selectedStacks.add(value);
        }

        // 히든 인풋에 콤마형태 문자열 저장
        $('#techStacksInput').val(Array.from(selectedStacks).join(','));
    });
    // -------- 기술 스택 선택 기능 끝 --------


    function isFormModified() {
        const inputChanged = $('#floatingSidebarContent')
            .find('input[type="text"], input[type="date"], textarea')
            .toArray()
            .some(el => el.value.trim() !== (el.defaultValue?.trim?.() || ''));

        if (inputChanged) return true;

        const selectChanged = $('#floatingSidebarContent')
            .find('select')
            .toArray()
            .some(select => {
                let initialValue = '';
                $(select).find('option').each(function () {
                    if (this.defaultSelected) {
                        initialValue = this.value;
                        return false;
                    }
                });
                return $(select).val() !== initialValue;
            });

        return selectChanged;
    }

    function attemptCloseFloatingSidebar(e) {
        if (isClosing) return; // 중복 실행 방지
        isClosing = true;

        if (e) {
            e.preventDefault();
            e.stopPropagation();
        }

        if (isFormModified()) {
            if (!confirm('작성 중인 내용을 취소하시겠습니까?')) {
                isClosing = false; // 취소 시 플래그 초기화
                return;
            }
        }

        $('#floatingOverlay').removeClass('show');
        $('#floatingSidebar').removeClass('show');
        $('#floatingSidebarContent').empty();

        setTimeout(() => {
            isClosing = false; // 완료 후 플래그 해제 (이벤트 중복 대비)
        }, 200); // 200ms 후 플래그 해제
    }

    // 취소 버튼
    $(document).on('click', '.cancel-button', function (e) {
        attemptCloseFloatingSidebar(e);
    });

    // ESC 키
    $(document).on('keyup', function (e) {
        if (e.key === "Escape") {
            attemptCloseFloatingSidebar(e);
        }
    });

    // 오버레이 더블클릭
    $(document).on('dblclick', '#floatingOverlay', function (e) {
        attemptCloseFloatingSidebar(e);
    });
});

