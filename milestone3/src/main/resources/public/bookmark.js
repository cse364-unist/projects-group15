let bookmarkCount = 1;

// Function to add a new bookmark card
function addBookmarkCard() {
    const bookmarkName = prompt('Enter the name of the bookmark:');
    if (bookmarkName) {
        bookmarkCount++;
        const bookmarkContainer = document.getElementById('bookmarkContainer');
        const newCard = document.createElement('div');
        newCard.className = 'bookmark-card';
        newCard.innerHTML = `
            <div style="display: flex; align-items: center; justify-content: space-between; width: 100%;">
                <h2>${bookmarkName}</h2>
                <div class="remove-card" onclick="confirmRemoveBookmark(this)">-</div>
            </div>
            <div class="movie-title">
                <a href="#" onclick="navigateToDetails('Movie${bookmarkCount}')">Movie${bookmarkCount}</a>
                <div class="remove-movie" onclick="confirmRemoveMovie(this)">-</div>
            </div>
            <div class="add-movie" onclick="addMovie(this)">+</div>
        `;
        bookmarkContainer.insertBefore(newCard, bookmarkContainer.lastElementChild);
    }
}

// Function to confirm removal of a bookmark card
function confirmRemoveBookmark(removeIcon) {
    const confirmation = confirm("Are you sure you want to remove this bookmark?");
    if (confirmation) {
        removeBookmark(removeIcon);
    }
}

// Function to remove a bookmark card
function removeBookmark(removeIcon) {
    const card = removeIcon.closest('.bookmark-card');
    card.parentNode.removeChild(card);
}

// Function to confirm removal of a movie from a bookmark card
function confirmRemoveMovie(removeMovieIcon) {
    const confirmation = confirm("Are you sure you want to remove this movie?");
    if (confirmation) {
        removeMovie(removeMovieIcon);
    }
}

// Function to remove a movie from a bookmark card
function removeMovie(removeMovieIcon) {
    const movie = removeMovieIcon.closest('.movie-title');
    movie.parentNode.removeChild(movie);
}

// Function to add a movie to a bookmark card
function addMovie(addMovieButton) {
    const movieTitle = prompt('Enter the movie title:');
    if (movieTitle) {
        const movieElement = document.createElement('div');
        movieElement.className = 'movie-title';
        movieElement.innerHTML = `
            <a href="#" onclick="navigateToDetails('${movieTitle}')">${movieTitle}</a>
            <div class="remove-movie" onclick="confirmRemoveMovie(this)">-</div>`;
        addMovieButton.parentNode.insertBefore(movieElement, addMovieButton);
    }
}

// Function to navigate to movie-details.html with movie name as a parameter
function navigateToDetails(movieTitle) {
    const url = new URL('movie-details.html', window.location.origin);
    url.searchParams.append('title', movieTitle);
    window.location.href = url.toString();
}
