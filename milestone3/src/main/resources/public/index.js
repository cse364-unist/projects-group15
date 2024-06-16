$(document).ready(function() {
    function fetchMovies(category) {
        let url = 'http://localhost:8080/cse364-project/getRecommendationBy' + category;
        var queryParams;

        switch (category) {
            case 'Season':
                $('#recommendation_h2').html('<h2 style="font-size: 1.75rem; color: #333; margin: 20px 0;">Recommendation by season</h2>');
                queryParams = {
                    month: currentMonth
                }
                break;
            case 'Time':
                $('#recommendation_h2').html('<h2 style="font-size: 1.75rem; color: #333; margin: 20px 0;">Recommendation by time</h2>');
                queryParams = {
                    hour: currentTime
                }
                break;
            case 'MovieId':
                $('#recommendation_h2').html('<h2 style="font-size: 1.75rem; color: #333; margin: 20px 0;">Recommendation by a movie in your bookmarks</h2>');
                queryParams = {
                    movieId: randomMovieId
                }
                break;
            case 'Info':
                $('#recommendation_h2').html('<h2 style="font-size: 1.75rem; color: #333; margin: 20px 0;">Recommendation by your information</h2>');
                queryParams = {
                    userId: userId
                }
                break;
        }

        $.ajax({
            url: url,
            type: 'GET',
            data: queryParams,
            dataType: 'json',
            success: function(movies) {
                displayMovies(movies);
            },
            error: function() {
                $('#movie-list').html('<p>Failed to bring in the movie.</p>');
            }
        });
    }

    function displayMovies(movies) {
        let content = '';
        movies.forEach((movie, index) => {
            if (index < 20) {
                content += `<div class="movie-item" style="flex-basis: 20%; padding: 10px; box-sizing: border-box;">
                        <h4>${movie.title}</h4>
                        <p>${movie.genre}</p>
                        <a href="movie-details.html?id=${movie.movieId}">Details</a>
                    </div>`;
            }
        });
        $('#movie-list').html(content);
    }

    function getRandomMovieIdFromRandomList(userMovieList) {
        if (!userMovieList || Object.keys(userMovieList).length === 0) {
            return undefined;
        }
        const keys = Object.keys(userMovieList);
        if (keys.length === 0) return undefined;
        const randomListName = keys[Math.floor(Math.random() * keys.length)];
        const movieIds = userMovieList[randomListName];
        if (!movieIds || movieIds.length === 0) return undefined;
        return movieIds[Math.floor(Math.random() * movieIds.length)];
    }

    function performSearch() {

        var queryParams = {
            userId: localStorage.getItem("userId"),
            line: $('#search-bar').val(),
            containingLists: $('input[name="selected-lists"]:checked').map(function() {
                return this.value.toLowerCase();
            }).get().join('|'),
            containingGenres: $('input[name="selected-genres"]:checked').map(function() {
                return this.value.toLowerCase();
            }).get().join('|'),
            filteringLists: $('input[name="filtered-lists"]:checked').map(function() {
                return this.value.toLowerCase();
            }).get().join('|'),
            filteringGenres: $('input[name="filtered-genres"]:checked').map(function() {
                return this.value.toLowerCase();
            }).get().join('|')
        };

        $.ajax({
            url: 'http://localhost:8080/cse364-project/getSearching',
            type: 'GET',
            data: queryParams,
            dataType: 'json',
            success: function(movies) {
                let content = '';
                movies.forEach((movie, index) => {
                    if (index < 20) {
                        content += `<div class="search-item" style="flex-basis: 20%; padding: 10px; box-sizing: border-box;">
                                <h4>${movie.movie.title}</h4>
                                <p>${movie.movie.genre}</p>
                                <a href="movie-details.html?id=${movie.movie.movieId}">Details</a>
                            </div>`;
                    }
                });
                $('#search-list').html(content);
            },
            error: function() {
                $('#search-list').html('<p>Failed to bring in the movie.</p>');
            }
        });
    }

    function logout() {
        localStorage.removeItem('userId');
        localStorage.removeItem('userMovieList');
        window.location.href = 'login.html';
    }

    const now = new Date();
    const currentMonth = now.getMonth() + 1
    const currentTime = now.getHours()
    const userMovieList = JSON.parse(localStorage.getItem("userMovieList"))
    const randomMovieId = getRandomMovieIdFromRandomList(userMovieList);
    const userId = localStorage.getItem("userId");

    const categories = ['Season', 'Time', 'MovieId', 'Info'];
    if (!randomMovieId) {
        categories.splice(categories.indexOf('MovieId'), 1);
    }
    const randomCategory = categories[Math.floor(Math.random() * categories.length)];
    fetchMovies(randomCategory);

    for (let listName in userMovieList) {
        $('#selected-list-options').append(`
            <label style="margin-right: 8px"><input type="checkbox" name="selected-lists" value="${listName}">${listName}</label><br>
        `);
    }
    for (let listName in userMovieList) {
        $('#filtered-list-options').append(`
            <label style="margin-right: 8px"><input type="checkbox" name="filtered-lists" value="${listName}">${listName}</label><br>
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
    $('#logout').click(logout);
    $('#search-button').click(performSearch);
});

