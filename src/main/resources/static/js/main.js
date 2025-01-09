$(function () {
    deleteUser();
    formateDate();
});

function deleteUser() {
    $(document).on("click", "button[data-id]", function () {
        const userId = $(this).data("id");

        if (confirm("Are you sure you want to delete this user?")) {
            window.location.href = '/admin/delete-user?id=' + userId;
        }

    });
}

function formateDate() {
    $('.order-date').each(function () {
        const rawDate = new Date($(this).text().trim());
        const formattedDate = rawDate.toLocaleString();
        $(this).text(formattedDate);
    });
}
