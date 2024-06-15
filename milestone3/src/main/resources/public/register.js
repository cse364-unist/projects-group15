$(document).ready(function() {
    $('#register-form').submit(function(event) {
        event.preventDefault();
        var queryParams = {
            userId: $('#userId').val(),
            gender: $('#gender').val(),
            age: convertAgeToCategory($('#age').val()),
            occupation: $('#occupation').val(),
            username: $('#username').val(),
            password: $('#password').val()
        };

        $.ajax({
            url: 'http://localhost:8080/cse364-project/addNewUser',
            type: 'POST',
            data: queryParams,
            dataType: 'json',
            success: function(response) {
                alert('Registration successful!');
                window.location.href = 'login.html';
            },
            error: function() {
                alert('Registration failed!');
            }
        });
    });

    function convertAgeToCategory(age) {
        if (age <= 17) return "1";
        if (age <= 24) return "18";
        if (age <= 34) return "25";
        if (age <= 44) return "35";
        if (age <= 49) return "45";
        if (age <= 55) return "50";
        return "56";
    }
});