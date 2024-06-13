$(document).ready(function() {
    $('#register-form').submit(function(event) {
        event.preventDefault();
        const username = $('#username').val();
        const password = $('#password').val();
        const gender = $('#gender').val();
        const age = convertAgeToCategory($('#age').val());
        const occupation = $('#occupation').val();
        const myUrl = `http://localhost:8080/addNewUser?username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}&gender=${gender}&age=${age}&occupation=${occupation}`;

        // 서버에 회원가입 요청 보내기
        $.ajax({
            url: myUrl,
            type: 'POST',
            contentType: 'application/json',
            success: function(response) {
                alert('Registration successful!');
                window.location.href = '/login.html'; // 로그인 페이지로 리디렉션
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