
$(document).ready(function() {
    $('#user-info-form').on('submit', function(event) {
        event.preventDefault();


        const userInfo = {
            username: $('#username').val(),
            gender: $('#gender').val(),
            occupation: $('#occupation').val(),
            age: $('#age').val()
        };


        const password = $('#password').val();
        if (password) {
            userInfo.password = password;
        }


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
                alert('Failed to update user information.');
                console.error(error);
            }
        });


        if (password) {
            $.ajax({
                url: 'http://localhost:8080/cse364-project/changeUserPassword',
                type: 'PUT',
                data: $.param({ username: $('#username').val(), password: password }),
                dataType: 'json', 
                success: function(response) {
                    alert('Password updated successfully!');
                    console.log(response);
                },
                error: function(error) {
                    alert('Failed to update password.');
                    console.error(error);
                }
            });
        }
    });
});
