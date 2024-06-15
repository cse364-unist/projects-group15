$(document).ready(function() {
    $('#login-form').submit(function(event) {
        event.preventDefault();
        var queryParams = {
            userId: $('#userId').val(),
            password: $('#password').val()
        };

        $.ajax({
            url: 'http://localhost:8080/cse364-project/passwordMatch',
            type: 'GET',
            data: queryParams,
            dataType: 'json',
            success: function(response) {
                localStorage.setItem('userId', response.userId);
                localStorage.setItem('userMovieList', JSON.stringify(response.movieList));
                window.location.href = '/cse364-project'
            },
            error: function() {
                alert('Login failed!');
            }
        });
    });
});