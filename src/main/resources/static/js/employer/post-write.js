$(document).ready(function () {
    let isClosing = false;

    // -------- 기술 스택 선택 기능 시작 --------
    const selectedStacks = new Set();

    $(document).on('click', '.stack-tag', function () {
        const value = $(this).data('value');

        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            selectedStacks.delete(value);
        } else {
            $(this).addClass('selected');
            selectedStacks.add(value);
        }

        $('#techStacksInput').val(Array.from(selectedStacks).join(','));
    });

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
        if (isClosing) return;
        isClosing = true;

        if (e) {
            e.preventDefault();
            e.stopPropagation();
        }

        if (isFormModified()) {
            if (!confirm('작성 중인 내용을 취소하시겠습니까?')) {
                isClosing = false;
                return;
            }
        }

        $('#floatingOverlay').removeClass('show');
        $('#floatingSidebar').removeClass('show');
        $('#floatingSidebarContent').empty();

        setTimeout(() => {
            isClosing = false;
        }, 200);
    }

    $(document).on('click', '.cancel-button', attemptCloseFloatingSidebar);
    $(document).on('keyup', function (e) {
        if (e.key === "Escape") attemptCloseFloatingSidebar(e);
    });
    $(document).on('dblclick', '#floatingOverlay', attemptCloseFloatingSidebar);

    // ✅ Quill 에디터 초기화 → 안전하게 DOM이 준비된 후 실행
    setTimeout(() => {
        const editorElement = document.querySelector('#quillEditor');
        if (editorElement) {
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
                    imageResize: { displaySize: true }
                }
            });

            // 기존 값 바인딩
            const existing = document.getElementById('description');
            if (existing && existing.value) {
                quill.root.innerHTML = existing.value;
            }

            // 전송 시 에디터 내용 저장
            document.querySelector('form').addEventListener('submit', function () {
                document.getElementById('description').value = quill.root.innerHTML;
            });
        }
    }, 100);
});
