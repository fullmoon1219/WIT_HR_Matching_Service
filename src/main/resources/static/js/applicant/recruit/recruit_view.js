// applicant/recruit/view.html

$(document).ready(function () {

	const jobPostId = getJobPostIdFromUrl();

	$.ajax({
		url: `/api/recruit/${jobPostId}`,
		method: 'GET',
		success: function (data) {

			const jobPost = data.jobPost;
			const employer = data.employer;

			$('#title').text(jobPost.title);
			$('#description').text(jobPost.description);
			$('#requiredSkills').text(jobPost.requiredSkills);
			$('#employmentType').text(translateEmploymentType(jobPost.employmentType));
			$('#jobCategory').text(jobPost.jobCategory);
			$('#salary').text(jobPost.salary);
			$('#location').text(jobPost.location);
			$('#deadline').text(jobPost.deadline);
			$('#createAt').text(jobPost.createAt);
			$('#viewCount').text(jobPost.viewCount);
			$('#bookmarkCount').text(jobPost.bookmarkCount);

			$('#companyName').text(employer.companyName);
			$('#address').text(employer.address);
			$('#phoneNumber').text(employer.phoneNumber);
			$('#homepageUrl').text(employer.homepageUrl);
			$('#industry').text(employer.industry);
			$('#companySize').text(employer.companySize);
		},
		error: function (xhr) {
			if (xhr.status === 403) {
				location.href = '/error/access-denied';
			} else if (xhr.status === 404) {
				location.href = '/error/not-found';
			} else {
				location.href = '/error/db-access-denied';
				console.error(xhr);
			}
		}
	});
});

function getJobPostIdFromUrl() {
	const parts = window.location.pathname.split('/');
	return parts[parts.length - 1];
}

function translateEmploymentType(code) {
	switch (code) {
		case 'FULLTIME': return '정규직';
		case 'PARTTIME': return '파트타임';
		case 'INTERN': return '인턴';
		case 'FREELANCE': return '프리랜서';
		default: return '기타';
	}
}