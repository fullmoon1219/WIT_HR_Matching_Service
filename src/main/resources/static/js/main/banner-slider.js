let currentSlide = 0;
let slideLinks;
let dots;
let slideInterval;

function showSlide(index) {
    slideLinks.forEach((link, i) => {
        link.classList.toggle('active', i === index);
        dots[i].classList.toggle('active', i === index);
    });
}

function changeSlide(direction) {
    currentSlide = (currentSlide + direction + slideLinks.length) % slideLinks.length;
    showSlide(currentSlide);
}

function startAutoSlide() {
    slideInterval = setInterval(() => {
        changeSlide(1);
    }, 5000);
}

document.addEventListener('DOMContentLoaded', () => {
    slideLinks = document.querySelectorAll('.slide-link');
    dots = document.querySelectorAll('.dot');

    dots.forEach((dot, i) => {
        dot.addEventListener('click', () => {
            currentSlide = i;
            showSlide(i);
        });
    });

    showSlide(currentSlide);
    startAutoSlide();
});