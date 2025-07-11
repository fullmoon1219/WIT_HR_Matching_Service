// applicant/resume/view.html

$(document).ready(function () {

	const resumeId = getIdFromUrl();

	$.ajax({
		url: `/api/resumes/${resumeId}`,
		method: 'GET',
		success: function (resume) {
			$('#title').text(resume.title);
			$('#education').text(resume.education);
			$('#experience').text(resume.experience);
			$('#skills').text(resume.skills);
			$('#preferredLocation').text(resume.preferredLocation);
			$('#salaryExpectation').text(resume.salaryExpectation);
			$('#coreCompetency').text(resume.coreCompetency);
			$('#desiredPosition').text(resume.desiredPosition);
			$('#motivation').text(resume.motivation);
			$('#createAt').text(resume.createAt);

			// 수정일은 등록일과 날짜가 다를 때 노출
			if (resume.updateAt && resume.updateAt !== resume.createAt) {
				$('#updatedAt').text(resume.updateAt);
				$('#updatedAtRow').show();
			}

			if (resume.isPublic === true) {
				$('#editBtn').hide();
				$('#deleteBtn').hide();
			}

			$('body').show();
		},
		error: function (xhr) {
			if (xhr.status === 403) {
				// 권한 없는 경우
				location.href = '/error/access-denied';
			} else if (xhr.status === 404) {
				// 없는 이력서
				location.href = '/error/not-found';
			} else {
				// 그 외 에러
				location.href = '/error/db-access-denied';
				console.error(xhr);
			}
		}
	});

	$('#editBtn').on('click', function () {
		location.href = `/applicant/resume/edit/${resumeId}`;
	});

	$('#deleteBtn').on('click', function () {

		if (confirm('정말 삭제하시겠습니까?')) {

			$.ajax({
				url: `/api/resumes/${resumeId}`,
				method: 'DELETE',
				success: function () {

					sessionStorage.setItem('toastMessage', '이력서가 삭제되었습니다.');
					location.href = '/applicant/resume/list';

				},
				error: function (xhr) {
					if (xhr.status === 403) {
						location.href = '/error/access-denied';
					} else if (xhr.status === 404) {
						location.href = '/error/not-found';
					} else {
						alert('이력서 삭제에 실패했습니다. 나중에 다시 시도해주세요.');
						console.error(xhr);
					}
				}
			});
		}
	})
});
