$(document).ready(function () {
    // ✅ 드롭다운 열기/닫기
    $('.dropdown-toggle').on('click', function (e) {
        e.stopPropagation();
        const current = $(this).next('.dropdown-content');
        $('.dropdown-content').not(current).slideUp();
        current.slideToggle();
    });

    // ✅ 드롭다운 내부 클릭 시 닫힘 방지
    $('.dropdown-content').on('click', function (e) {
        e.stopPropagation();
    });

    // ✅ 외부 클릭 시 닫기
    $(document).on('click', function () {
        $('.dropdown-content').slideUp();
    });

    // ✅ 체크박스 선택 시 미리보기 태그 표시
    $('input[type="checkbox"]').on('change', function () {
        updateSelectedTags();
    });

    // ✅ 태그 내 제거 버튼 클릭 시 체크 해제
    $(document).on('click', '.remove-tag', function () {
        const value = $(this).parent().data('value');
        const name = $(this).parent().data('name');
        $(`input[name="${name}"][value="${value}"]`).prop('checked', false);
        updateSelectedTags();
    });

    function updateSelectedTags() {
        const $locations = $('#selectedLocations');
        const $skills = $('#selectedSkills');
        $locations.empty();
        $skills.empty();

        $('input[type="checkbox"]:checked').each(function () {
            const name = $(this).attr('name');
            const value = $(this).val();
            const label = $(this).data('name') || value;

            const tag = `
                <span class="tag" data-name="${name}" data-value="${value}">
                    ${label}
                    <span class="remove-tag">×</span>
                </span>
            `;
            if (name === 'preferred_location') {
                $locations.append(tag);
            } else if (name === 'skills') {
                $skills.append(tag);
            }
        });
    }

    // ✅ CSRF 토큰 자동 설정 (Spring Security 대응)
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr) {
        xhr.setRequestHeader(header, token);
    });

    // ✅ 폼 제출 → Ajax 처리
    $('#searchForm').on('submit', function (e) {
        e.preventDefault();
        const url = $(this).attr('action');
        const formData = $(this).serialize();

        $.ajax({
            url: url,
            method: 'POST',
            data: formData,
            success: function (res) {
                renderTable(res.resumes);
                renderTechStackDropdown(res.stackList, res.selectedSkillIds);
                updateSelectedTags();
            },
            error: function () {
                alert('검색 요청에 실패했습니다.');
            }
        });
    });

    // ✅ 결과 테이블 렌더링 (skills 문자열 대응)
    function renderTable(data) {
        const $tbody = $('.resume-result-table tbody');
        $tbody.empty();

        if (!data || data.length === 0) {
            $tbody.append(`<tr><td colspan="6">검색 결과가 없습니다.</td></tr>`);
            return;
        }

        data.forEach((resume, idx) => {
            const skillArray = resume.skillNames || [];

            const skillsHtml = skillArray.map(skill =>
                `<span class="skill-tag">${skill}</span>`
            ).join('');

            const row = `
            <tr>
                <td>${idx + 1}</td>
                <td><a href="#" class="edit-post-link" data-id="${resume.resumeId}">${resume.title}</a></td>
                <td>${resume.name}</td>
                <td>${resume.education}</td>
                <td>${resume.preferredLocation}</td>
                <td>${skillsHtml}</td>
            </tr>
        `;
            $tbody.append(row);
        });
    }

    // ✅ 기술 스택 드롭다운 재구성
    function renderTechStackDropdown(stackList, selectedSkillIds) {
        const ids = Array.isArray(selectedSkillIds) ? selectedSkillIds : [];

        const $container = $('#techDropdown');
        $container.empty();

        stackList.forEach(stack => {
            const isChecked = ids.includes(stack.id) || ids.includes(String(stack.id)) ? 'checked' : '';
            const label = `
            <label>
                <input type="checkbox" name="skills" value="${stack.id}" data-name="${stack.name}" ${isChecked} />
                ${stack.name}
            </label>
        `;
            $container.append(label);
        });

        $('input[name="skills"]').off('change').on('change', function () {
            updateSelectedTags();
        });
    }

    // ✅ 이력서 제목 클릭 시 상세 보기 (floating 패널 열기)
    $(document).on('click', '.edit-post-link', function (e) {
        e.preventDefault();

        const resumeId = $(this).data('id');

        $.ajax({
            url: '/employer/publicResume/resume_detail',
            method: 'GET',
            data: { resumeId: resumeId },
            success: function (html) {
                $('#floatingSidebarContent').html(html);
                $('#floatingOverlay').show();
                $('#floatingSidebar').addClass('show');
            },
            error: function () {
                alert('이력서 상세 정보를 불러오는 데 실패했습니다.');
            }
        });
    });

    // ✅ 패널 외부 클릭 시 닫기
    $('#floatingOverlay').on('click', function () {
        $('#floatingSidebar').removeClass('show');
        $(this).hide();
    });
});