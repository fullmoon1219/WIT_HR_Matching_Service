<!-- 오른쪽 이동 네비게이션 -->

document.addEventListener('DOMContentLoaded', () => {
    const navLinks = document.querySelectorAll('.section-nav .nav-link');

    navLinks.forEach(link => {
    link.addEventListener('click', function (e) {
    const href = this.getAttribute('href');
    if (href.startsWith('#')) {
    e.preventDefault();

    const targetId = href.slice(1);

    // TOP 버튼일 경우: 바로 0으로 이동
    if (targetId === 'header') {
    window.scrollTo({
    top: 0,
    behavior: 'smooth'
});
    return;
}

    const target = document.getElementById(targetId);
    const offset = 160;

    if (target) {
    const targetTop = target.getBoundingClientRect().top + window.scrollY;
    window.scrollTo({
    top: targetTop - offset,
    behavior: 'smooth'
});
}
}
});
});
});
