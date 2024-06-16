
$(document).ready(function() {
    $('#user-info-form').on('submit', function(event) {
        event.preventDefault();

        let originalPassword;

        $.ajax({
            url: 'http://localhost:8080/cse364-project/getUser',
            type: 'GET',
            data: { userId: localStorage.getItem('userId') },
            dataType: 'json',
            success: function(response) {
                originalPassword = response.password
                if (originalPassword !== $('#passwordOld').val()) return;
                const userInfo = {
                    username: $('#username').val(),
                    gender: $('#gender').val(),
                    occupation: $('#occupation').val(),
                    age: $('#age').val(),
                    passwordNew: $('#passwordNew').val()
                };

                Object.keys(userInfo).forEach(function(key) {
                    if (userInfo[key] !== '' && key !== 'passwordNew') {
                        $.ajax({
                            url: 'http://localhost:8080/cse364-project/changeUserInfo',
                            type: 'PUT',
                            data: { value: userInfo[key], userId: localStorage.getItem('userId'), type: key },
                            dataType: 'json',
                            success: function(response) {
                                console.log('Updated ' + key, response);
                            },
                            error: function(error) {
                                alert('Failed to update ' + key);
                                console.error(error);
                                return;
                            }
                        });
                    }
                });

                alert('User information updated successfully!');

                if (userInfo.passwordNew) {
                    $.ajax({
                        url: 'http://localhost:8080/cse364-project/changeUserPassword',
                        type: 'PUT',
                        data: { passwordNew: userInfo.passwordNew, userId: localStorage.getItem('userId') , passwordOld: originalPassword },
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
            },
            error: function() {
                alert('Error');
            }
        });


    });
});

function logout() {
    localStorage.removeItem('userId');
    localStorage.removeItem('userMovieList');
    window.location.href = 'login.html';
}

$('#logout').click(logout);