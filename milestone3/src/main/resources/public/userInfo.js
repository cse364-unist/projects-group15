$(document).ready(function() {
    $('#user-info-form').on('submit', function(event) {
        event.preventDefault();

        const userInfo = {
            username: $('#username').val(),
            gender: $('#gender').val(),
            occupation: $('#occupation').val(),
            age: $('#age').val()
        };

        const queryParams = $.param(userInfo);

        $.ajax({
            url: 'http://localhost:8080/cse364-project/changeUserInfo',
            type: 'PUT',
            data: queryParams,
            dataType: 'json',
            success: function(response) {
                alert('User information updated successfully!');
                console.log(response);
            },
            error: function(error) {
                alert('Failed to update user information. Please try again');
                console.error(error);
            }
        });
    });
});
