$(document).ready(function() {
    function fetchMovies(category) {
        let url = `http://localhost:8080/movies/${category}`;

        switch (category) {
            case 'season':
                url += `/${currentMonth}`;
                break;
            case 'time':
                url += `/${currentTime}`;
                break;
            case 'movie':
                url += `/${randomMovieId}`;
                break;
            case 'info':
                url += `/${userId}`;
                break;
        }

        $.ajax({
            url: url,
            type: 'GET',
            success: function(movies) {
                displayMovies(movies);
            },
            error: function() {
                $('#movie-list').html('<p>영화를 불러오는데 실패했습니다.</p>');
            }
        });
    }

    function displayMovies(movies) {
        let content = '';
        movies.forEach((movie, index) => {
            if (index < 20) {  // 최대 20개의 영화 표시
                content += `<div class="movie-item"><h4>${movie.title}</h4><p>${movie.genre}</p><a href="movie-details.html?id=${movie.id}">Details</a></div>`;
            }
        });
        $('#movie-list').html(content);
    }

    function getRandomMovieIdFromRandomList(userMovieList) {
        if (!userMovieList) return
        const keys = Object.keys(userMovieList); // 객체의 모든 키를 배열로 반환
        const randomListName = keys[Math.floor(Math.random() * keys.length)]; // 랜덤하게 리스트 이름 선택
        const movieIds = userMovieList[randomListName]; // 선택된 리스트의 영화 ID 목록
        const randomMovieId = movieIds[Math.floor(Math.random() * movieIds.length)]; // 영화 ID 목록에서 랜덤하게 영화 ID 선택
        return randomMovieId;
    }

    // 1~4 중 랜덤 추천 카테고리 선택

    const now = new Date();
    const currentMonth = now.getMonth() + 1
    const currentTime = now.getHours()
    const userMovieList = JSON.parse(localStorage.getItem("userMovieList"))
    const randomMovieId = getRandomMovieIdFromRandomList(userMovieList);
    const userId = localStorage.getItem("userId");

    const categories = ['season', 'time', 'movie', 'info'];
    const randomCategory = categories[Math.floor(Math.random() * categories.length)];
    fetchMovies(randomCategory);

    for (let listName in userMovieList) {
        $('#selected-list-options').append(`
            <label><input type="checkbox" name="selected-lists" value="${listName}">${listName}</label><br>
        `);
    }
    for (let listName in userMovieList) {
        $('#filtered-list-options').append(`
            <label><input type="checkbox" name="filtered-lists" value="${listName}">${listName}</label><br>
        `);
    }

    // Populate genre options (static for now, can be dynamic if needed)
    const genres = ["Action", "Adventure", "Animation", "Children's", "Comedy", "Crime", "Documentary", "Drama", "Fantasy", "Film-Noir", "Horror", "Musical", "Mystery", "Romance", "Sci-Fi", "Thriller", "War", "Western"]

    genres.forEach(genre => {
        $('#selected-genre-options').append(`<label style="margin-right: 8px"><input type="checkbox" name="selected-genres" value="${genre}">${genre}</label>`);
    });
    genres.forEach(genre => {
        $('#filtered-genre-options').append(`<label style="margin-right: 8px"><input type="checkbox" name="filtered-genres" value="${genre}">${genre}</label>`);
    });

    // Handle search and submission
    $('#search-button').click(performSearch);
});

function performSearch() {

    const userId = localStorage.getItem("userId");
    const line = $('#search-bar').val();
    const selectedLists = $('input[name="selected-lists"]:checked').map(function() { return this.value; }).get();
    const selectedGenres = $('input[name="selected-genres"]:checked').map(function() { return this.value; }).get();
    const filteredLists = $('input[name="filtered-lists"]:checked').map(function() { return this.value; }).get();
    const filteredGenres = $('input[name="filtered-genres"]:checked').map(function() { return this.value; }).get();
    console.log('Searching for:', line, selectedLists, selectedGenres);
    const myUrl = `http://localhost:8080//searching/${userId}/${encodeURIComponent(line)}?selectedLists=${encodeURIComponent(selectedLists)}&selectedGenres=${encodeURIComponent(selectedGenres)}&filteredLists=${encodeURIComponent(filteredLists)}&filteredGenres=${encodeURIComponent(filteredGenres)}`;
    $.ajax({
        url: myUrl,
        type: 'GET',
        success: function(movies) {

        },
        error: function() {
            $('#movie-list').html('<p>영화를 불러오는데 실패했습니다.</p>');
        }
    });
}