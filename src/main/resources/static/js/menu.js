const navItems = document.querySelectorAll('.nav-item');

function closeAllMenus() {
    navItems.forEach(item => item.classList.remove('active'));
}

navItems.forEach(item => {
    item.addEventListener('click', function (event) {
        event.stopPropagation();
        const isActive = this.classList.contains('active');
        closeAllMenus();
        if (!isActive) {
            this.classList.add('active');
        }
    });
});

document.addEventListener('click', function () {
    closeAllMenus();
});
