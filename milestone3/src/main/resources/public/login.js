$(document).ready(function() {
    $('#login-form').submit(function(event) {
        event.preventDefault();
        const username = $('#username').val();
        const password = $('#password').val();
        const myUrl = 'http://localhost:8080/users/' + username + "/" + password

        // 서버에 로그인 요청 보내기
        $.ajax({
            url: myUrl,
            type: 'GET',
            success: function(response) {
                localStorage.setItem('userId', response.userId);
                localStorage.setItem('userMovieList', response.movieList);
                window.location.href = '/index.html'
            },
            error: function() {
                alert('Login failed!');
            }
        });
    });
});