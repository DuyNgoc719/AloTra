document.addEventListener('DOMContentLoaded', function () {
    const menuItem = document.querySelector('.nav-item:nth-child(4)'); // Lấy phần tử nav-item thứ 4 (THỰC ĐƠN)
    const menuSection = menuItem.querySelector('.menu-section');

    menuItem.addEventListener('mouseenter', function () {
        menuSection.style.display = 'block'; // Hiện menu section
    });

    menuItem.addEventListener('mouseleave', function () {
        if (!menuSection.matches(':hover')) {
            menuSection.style.display = 'none'; // Ẩn menu section khi không hover
        }
    });

    menuSection.addEventListener('mouseenter', function () {
        menuSection.style.display = 'block'; // Giữ menu section hiện khi hover
    });

    menuSection.addEventListener('mouseleave', function () {
        menuSection.style.display = 'none'; // Ẩn menu section khi không hover
    });
});
