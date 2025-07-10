// /js/employer/post-modify.js

$(document).ready(function () {
    // -------- 수정 취소 버튼 기능 --------
    $(document).off('click', '.cancel-button').on('click', '.cancel-button', function (e) {
        e.preventDefault();

        const hasChanges = $('#floatingSidebarContent')
            .find('input, textarea, select')
            .toArray()
            .some(el => el.value !== el.defaultValue);

        if (hasChanges && !confirm('작성 중인 내용을 취소하시겠습니까?')) {
            return;
        }

        $.ajax({
            url: `/employer/jobpost_list`,
            method: 'GET',
            success: function (html) {
                $('#floatingSidebarContent').html(html);
                $('#floatingOverlay').removeClass('show');
                $('#floatingSidebar').removeClass('show');
            },
            error: function () {
                alert('리스트 페이지를 불러오는 데 실패했습니다.');
            }
        });
    });

    // -------- 기술 스택 선택 기능 --------
    const selectedStacks = new Set(
        $('#techStacksInput').val().split(',').map(v => v.trim()).filter(v => v !== "")
    );

    // 초기 selected 클래스 적용
    $('.stack-tag').each(function () {
        const value = $(this).data('value').toString();
        if (selectedStacks.has(value)) {
            $(this).addClass('selected');
        }
    });

    // 선택/해제 토글
    $(document).on('click', '.stack-tag', function () {
        const value = $(this).data('value').toString();

        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            selectedStacks.delete(value);
        } else {
            $(this).addClass('selected');
            selectedStacks.add(value);
        }

        $('#techStacksInput').val(Array.from(selectedStacks).join(','));
    });

    /*// -------- Quill 에디터 초기화 + 기존 값 바인딩 + 전송 처리 --------
    const quill = new Quill('#quillEditor', {
        theme: 'snow',
        placeholder: '상세 설명을 입력하세요...',
        modules: {
            toolbar: [
                [{ header: [1, 2, false] }],
                ['bold', 'italic', 'underline'],
                ['image', 'code-block'],
                [{ list: 'ordered' }, { list: 'bullet' }],
                ['link']
            ],
            imageResize: {
                displaySize: true
            }
        }
    });*/

   /* // 상세설명 기존 내용 바인딩
    const descriptionContent = $('#description').val();
    quill.root.innerHTML = descriptionContent;

    // ✅ 폼 전송 시 Quill 내용을 hidden input에 저장
    $('form').on('submit', function () {
        const html = quill.root.innerHTML;
        $('#description').val(html);
    });*/

});
