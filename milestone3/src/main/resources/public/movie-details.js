$(document).ready(function() {
    // URL에서 영화 ID 가져오기
    const queryParams = new URLSearchParams(window.location.search);
    const movieId = queryParams.get('id');

    if (movieId) {
        $.ajax({
            url: `http://localhost:8080/movies/${movieId}`,
            type: 'GET',
            success: function(movie) {
                $('#movie-info').html(`
                    <h1>${movie.movieName}</h1>
                    <p>${movie.movieGenre}</p>
                `);
            },
            error: function() {
                $('#movie-info').html('<p>영화 정보를 불러오는데 실패했습니다.</p>');
            }
        });
    } else {
        $('#movie-info').html('<p>영화 ID가 제공되지 않았습니다.</p>');
    }
});