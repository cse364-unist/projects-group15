function display() {
    const bookmarkContainer = document.getElementById('bookmarkContainer');
    while (bookmarkContainer.firstChild && bookmarkContainer.children.length > 1) {
        bookmarkContainer.removeChild(bookmarkContainer.firstChild);
    }
    const queryParams = {
        userId: localStorage.getItem('userId')
    };
    $.ajax({
        url: 'http://localhost:8080/cse364-project/getUser',
        type: 'GET',
        data: queryParams,
        dataType: 'json',
        success: function(response) {
            localStorage.setItem('userMovieList', JSON.stringify(response.movieList));
            Object.entries(response.movieList).forEach(([listName, movies]) => {
                const newCard = document.createElement('div');
                newCard.className = 'bookmark-card';
                let moviesPromises = movies.map(movie => {
                    return new Promise((resolve, reject) => {
                        $.ajax({
                            url: 'http://localhost:8080/cse364-project/getMovie',
                            type: 'GET',
                            data: { movieId: movie },
                            dataType: 'json',
                            success: function(movieDetails) {
                                resolve(`
                                    <div class="movie-title">
                                        <a href="#" onclick="navigateToDetails('${movieDetails.movieId}')">${movieDetails.movieName}</a>
                                        <div class="remove-movie" onclick="confirmRemoveMovie('${listName}', '${movies.indexOf(movie)}')">-</div>
                                    </div>
                                `);
                            },
                            error: reject
                        });
                    });
                });

                Promise.all(moviesPromises).then(moviesHTML => {
                    newCard.innerHTML = `
                        <div style="display: flex; align-items: center; justify-content: space-between; width: 100%;">
                            <h2>${listName}</h2>
                            ${moviesHTML.join('')}
                        </div>
                    `;
                    bookmarkContainer.insertBefore(newCard, bookmarkContainer.lastElementChild);
                }).catch(() => {
                    alert('Unexpected Error');
                });
            });
        },
        error: function() {
            alert('Unexpected Error');
        }
    });
}

function addBookmarkCard() {
    const bookmarkName = prompt('Enter bookmark name:');
    var queryParams = {
        listname: bookmarkName,
        userId: localStorage.getItem('userId')
    }
    $.ajax({
        url: 'http://localhost:8080/cse364-project/addMovieList',
        type: 'PUT',
        data: queryParams,
        dataType: 'json',
        success: function(response) {
            display()
        },
        error: function(error) {
            alert('Unexpected Error');
        }
    });
}

function confirmRemoveMovie(name, index) {
    const confirmation = confirm("Do you want to remove this movie?");
    if (confirmation) {
        removeMovie(name, index);
    }
}

function removeMovie(name, index) {
    const queryParams = {
        userId: localStorage.getItem('userId'),
        listname: name,
        index: index
    }
    $.ajax({
        url: 'http://localhost:8080/cse364-project/removeElementFromMovieList',
        type: 'PUT',
        data: queryParams,
        dataType: 'json',
        success: function(response) {
            display()
        },
        error: function(error) {
            alert('Unexpected Error');
        }
    });
}

function navigateToDetails(movieId) {
    const url = 'movie-details.html?id=' + movieId
    window.location.href = url;
}

display()