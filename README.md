# MovieSearcher

## 1. About this application

There are many people whose hobby is watching movie. There are a lot of movies, but not all movies are good. There are movies which user review score is bad, and even if the movie's user score is good, the movie may not be in user's taste. So users can waste time, money, and effort when they watch movies which they do not like. To prevent this, users should watch movies which are in their taste. Our application will help users finding the right movie which they want to watch by processing review data, bookmarks, and recommending algorithm.

## 2. Features

There are three features, review visualization, bookmarks, and recommending movies.

### 1. Review visualization

When users enter the search keyword, then the list of the movies will appear. After that, user can select one of the movies. When users select the movies, the review data will be visualized and appeared. User can choose the topic, for example, ratio of number of users who gave reviews, ratio of users who gave five-star reviews, review distribution of certain group of users... For ratio, the result is given in pie chart, for distribution, the result is given in bar graph.

### 2. Bookmarks

Users can store the list of movies. At first, there is no bookmark list, when users press the add button, then the empty bookmark will be added. Users can add or remove movies in the bookmark list. Users can also delete bookmarks,

### 3. Recommending movies

This feature recommends movies to users. We applied the simple algorithm. The algorithm is first, finding users with same taste, if the users are not searched, second, the algorithm will use data of the movie that other people see at a certain time or date or season and has a higher rating. And then, the algorithm will pick the movies from users' bookmarks.

## 3. How does it work

### Web Page

Actions in the web page will be done by two conditions, receiving HTTP response, and pressing some of the buttons.

#### Sign up page.

The sign up page contains forms and sign up button. There are forms of username, occupation, gender, age, ID, password, password retype. When users press sign up button, then the web will check whether all of the forms are filled. When all of the forms are not filled, then the web page will send the alert. When all of the form are filled, then web page will send the HTTP request into the server. The server will add a user into user database.

#### Log in page.

The log in page contains ID, password forms and login button. When users press the log in button, then the web will check whether all of the forms are filled, When all of the form are not filled, then the web page will send the alert. When all of the form are filled, then web page will send the HTTP request into the server. Then the server will send the HTTP response whether the login succeeded.

#### Review Visualization Search Page

This page contains search form and search button. When users press the search button, then the web will check whether the search form is filled, when search form is filled then the web page will send the HTTP request into the server. The server will send response of the results and the web page will display list of movies. When users press the one of the movies, then web page will send the HTTP request to get reviews of the movies. The server will send the HTTP response of lists of movies.

#### Review Visualization Result Page

After the web page get response, then the visualized results will be displayed the data of the result is stored in temporary variable. There are some topics of the review visualization. When users select one of the topics, then the result is filtered in the variable by age, gender, or ratings, etc... After that the filtered results will be shown. The ratio data will be shown in pie chart, the distribution data will be shown in bar graph.

#### Bookmark Page

At first there will be a empty bookmark page. The empty bookmark page contains add button. When users press the add button, then the web page will send a HTTP request about adding bookmark. The server will receive the HTTP request then it will add the empty bookmark of the current user's database and send the HTTP response of bookmark data of the current user. After that, users can edit the bookmark. Every time the bookmark is added, edited, or deleted, the web page will send the HTTP request to server, and the server will apply changes to database and send HTTP response of the current data of user's bookmark.

#### Movie Recommendation Page

The movie recommendation page has similar layout with movie visulalization search page. There are search form and search button. When users press the search button, then the web will check whether the search form is filled, when search form is filled then the web page will send the HTTP request into the server. Then the server will receive the HTTP request and send a HTTP response about recommended movie list. The is done by certain algorithm. The algorithm will first find users with similar taste and make the list of movies from bookmarks of users with similar taste. Second it will find review of the certain time (date, season) and find movies with highest rating. Finally, it will apply user's bookmark.

### Server

The application uses mongodb for database, the data of the db is stroed in dictionary type. For server, the application uses spring boot framework. When users press certain buttons, then the HTTP request is sent into the server. The server will evaluate and perform actions by java files, such as manipulation of data, finding search results... Then it will send the result by HTTP response. The web page will do actions like displaying visualized reviews, displaying bookmarks or movies when they received HTTP response.

## 4. How to use

### 1. Creating an Account

1. Go to sign up page.
2. Enter username, age, gender, occupation, zipcode, password and password confirm.
3. Press the sign up button (Password and password check should be equal)

### 2. Log in

1. Go to log in page.
2. Enter username and password.
3. Press the log in button.

### 3. Changing user information

1. Go to user info page.
2. Fill the form to change information.
3. Press the submit button.

### 4. Viewing review data

1. In the main page, there is a textbox and search button.
2. Enter the keyword in text button.
3. Press the search button.
4. Advanced options : There are filters below the search textbox and the button. Users can check the checkbox to filter the movies by genres
5. The searched movies will be appeared.
6. Recommended movies will bo appeared below the searched movies.

### 5. Bookmark page.

1. Press + button to add the bookmark.
2. The new bookmark block will be appeared.
3. Press + button inside the block to add the movie.
4. Press - button next to the title of the movie to delete the movie.
5. Press - button next to the title of the bookmark to delete the bookmark.

### 6. Movie Details

1. The details of the movies are viewed.
2. Users can view the bar or pie chart of the reviews.
3. There is the review writing section and submit button. users can submit the reviews.
4. There is adding to bookmark button below the review submit section, users can add the movie into the bookmark.