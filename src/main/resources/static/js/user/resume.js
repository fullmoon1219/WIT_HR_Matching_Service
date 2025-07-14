document.addEventListener('DOMContentLoaded', function () {
    const starButtons = document.querySelectorAll('.star-button');

    starButtons.forEach(button => {
        button.addEventListener('click', () => {
            // 모든 버튼 초기화
            starButtons.forEach(btn => {
                btn.classList.remove('selected');
                btn.textContent = '☆';
            });

            // 현재 클릭한 버튼만 활성화
            button.classList.add('selected');
            button.textContent = '★';
        });
    });
});
