let currentPage = 1

const totalPages = 5;

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById("questionnaireForm");

    showPage(1);
    const nextButtons = document.querySelectorAll('.btn-next');
    const prevButtons = document.querySelectorAll('.btn-prev');

    nextButtons.forEach(btn => {
        btn.addEventListener('click', nextPage);
    });

    prevButtons.forEach(btn => {
        btn.addEventListener('click', previousPage);
    });
    console.log('DOM Loaded');
});

function showPage(pageNumber) {
    //hides all pages
    const pages = document.querySelectorAll('.page');
    pages.forEach(page => page.classList.remove('active'))
    console.log(pages)
    //showing the current page
    const currentPageElement = document.querySelector(`[data-page="${pageNumber}"]`);
    if(currentPageElement) {
        currentPageElement.classList.add('active');
        console.log(currentPageElement)
        console.log('Page shown:', pageNumber);
    }

    updateButtons();

    // updateProgress();
}

function nextPage(){
    if(currentPage < totalPages){
        currentPage++;
        showPage(currentPage);
        console.log('Next page:', nextPage);
    }
}

function previousPage(){
    if(currentPage > 1){
        currentPage--;
        showPage(currentPage);
        console.log('Previous page:', previousPage);
    }
}

function updateButtons (){
    const prevButton = document.querySelector('.btn-prev');
    const nextButton = document.querySelector('.btn-next');

    if(prevButton){
        prevButton.disabled = currentPage === 1;
    }

    if(nextButton){
        nextButton.disabled = currentPage === totalPages;
    }
}
