$(document).ready(function() {
    const queryParams = new URLSearchParams(window.location.search);
    const queryParam = {
        movieId: queryParams.get('id')
    }

    $.ajax({
        url: 'http://localhost:8080/cse364-project/getMovie',
        type: 'GET',
        data: queryParam,
        success: function(movie) {
            console.log(movie);
            function drawPieChart(data, selector) {
                const width = 500, height = 500, radius = Math.min(width, height) / 2;
                const color = d3.scaleOrdinal(d3.schemeCategory10);
                const svg = d3.select(selector)
                    .append("svg")
                    .attr("width", width)
                    .attr("height", height)
                    .append("g")
                    .attr("transform", `translate(${width / 2},${height / 2})`);

                const pie = d3.pie().value(d => d.value);
                const path = d3.arc().outerRadius(radius).innerRadius(0);

                const tooltip = d3.select('body').append('div')
                    .attr('class', 'tooltip')
                    .style('opacity', 0)
                    .style('position', 'absolute')
                    .style('text-align', 'center')
                    .style('width', '360px')
                    .style('height', '42px')
                    .style('padding', '2px')
                    .style('font', '24px sans-serif')
                    .style('background', 'lightsteelblue')
                    .style('border', '0px')
                    .style('border-radius', '8px')
                    .style('pointer-events', 'none');

                const arc = svg.selectAll("arc")
                    .data(pie(data))
                    .enter()
                    .append("g");

                arc.append("path")
                    .attr("d", path)
                    .attr("fill", (d, i) => color(i))
                    .on('mouseover', function (event, d) {
                        tooltip.transition()
                            .duration(10)
                            .style('opacity', 1);
                        tooltip.html(`${d.data.label}: ${(d.data.value * 100).toFixed(2)}%`)
                            .style('left', (event.pageX) + 'px')
                            .style('top', (event.pageY - 28) + 'px');
                    })
                    .on('mousemove', function (event, d) {
                        tooltip
                            .style('left', (event.pageX) + 'px')
                            .style('top', (event.pageY - 28) + 'px');
                    })
                    .on('mouseout', function (d) {
                        tooltip.transition()
                            .duration(200)
                            .style('opacity', 0);
                    });
            }

            function drawBarChart(data, selector) {
                const margin = {top: 20, right: 20, bottom: 30, left: 40},
                    width = 960 - margin.left - margin.right,
                    height = 500 - margin.top - margin.bottom;

                const x = d3.scaleBand().rangeRound([0, width]).padding(0.1),
                    y = d3.scaleLinear().rangeRound([height, 0]),
                    color = d3.scaleOrdinal(d3.schemeCategory10);

                const svg = d3.select(selector).append("svg")
                    .attr("width", width + margin.left + margin.right)
                    .attr("height", height + margin.top + margin.bottom)
                    .append("g")
                    .attr("transform", `translate(${margin.left},${margin.top})`);

                x.domain(data.map(d => d.label));
                y.domain([0, d3.max(data, d => d.value)]);

                svg.append("g")
                    .attr("class", "x axis")
                    .attr("transform", `translate(0,${height})`)
                    .call(d3.axisBottom(x));

                svg.append("g")
                    .attr("class", "y axis")
                    .call(d3.axisLeft(y).ticks(10, "%"));

                const tooltip = d3.select('body').append('div')
                    .attr('class', 'tooltip')
                    .style('opacity', 0)
                    .style('position', 'absolute')
                    .style('text-align', 'center')
                    .style('width', '360px')
                    .style('height', '42px')
                    .style('padding', '2px')
                    .style('font', '24px sans-serif')
                    .style('background', 'lightsteelblue')
                    .style('border', '0px')
                    .style('border-radius', '8px')
                    .style('pointer-events', 'none');

                svg.selectAll(".bar")
                    .data(data)
                    .enter().append("rect")
                    .attr("class", "bar")
                    .attr("x", d => x(d.label))
                    .attr("width", x.bandwidth())
                    .attr("y", d => y(d.value))
                    .attr("height", d => height - y(d.value))
                    .style("fill", d => color(d.label))
                    .on('mouseover', function (event, d) {
                        tooltip.transition()
                            .duration(10)
                            .style('opacity', 1);
                        tooltip.html(`${d.label}: ${(d.value * 100).toFixed(2)}%`)
                            .style('left', (event.pageX) + 'px')
                            .style('top', (event.pageY - 28) + 'px');
                    })
                    .on('mousemove', function (event, d) {
                        tooltip
                            .style('left', (event.pageX) + 'px')
                            .style('top', (event.pageY - 28) + 'px');
                    })
                    .on('mouseout', function (d) {
                        tooltip.transition()
                            .duration(200)
                            .style('opacity', 0);
                    });
            }

            function submitRating() {
                const rating = $('#rating').val();
                const queryParams = {
                    rating: rating,
                    userId: localStorage.getItem("userId"),
                    movieId: movie.movieId
                };
                $.ajax({
                    url: 'http://localhost:8080/cse364-project/updateRating',
                    type: 'PUT',
                    data: queryParams,
                    dataType: 'json',
                    success: function(response) {
                        getCurrentRating();
                    },
                    error: function() {
                        $.ajax({
                            url: 'http://localhost:8080/cse364-project/addNewRating',
                            type: 'POST',
                            data: queryParams,
                            dataType: 'json',
                            success: function(response) {
                                getCurrentRating();
                            },
                            error: function() {
                                $('#ratingResult').html('<p>Unexpected Error.</p>');
                            }
                        });
                    }
                });
            }

            function getCurrentRating() {
                const rating = $('#rating').val();
                const queryParams = {
                    userId: localStorage.getItem("userId"),
                    movieId: movie.movieId
                };
                $.ajax({
                    url: 'http://localhost:8080/cse364-project/getRating',
                    type: 'GET',
                    data: queryParams,
                    dataType: 'json',
                    success: function(response) {
                        $('#currentRating').html(`<p>Your Rating: ${response.rating}</p>`);
                    },
                    error: function() {
                        $('#currentRating').html(`<p>Your Rating: No rating.</p>`);
                    }
                });
            }

            function performAdd() {
                const selectedLists = $('input[name="selected-lists"]:checked').get().join('|');
                for (let selectedListName in selectedLists) {
                    queryParams = {
                        element: queryParams.get('id'),
                        userId: localStorage.getItem("userId"),
                        listname: selectedListName
                    }
                    $.ajax({
                        url: 'http://localhost:8080/cse364-project/addElementToMovieList',
                        type: 'PUT',
                        data: queryParams,
                        dataType: 'json',
                        success: function() {
                            alert("Added successfully");
                        },
                        error: function() {
                            alert("Unexpected error");
                        }
                    });
                }
            }

            $('#movie-info1').html(`
                <h1>${movie.movieName}</h1>
                <p>${movie.movieGenre}</p>
                <div id="currentRating"></div>
                <label for="rating">New Rating:</label>
                <select id="rating">
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                </select>
            `);
            $('#rating-button').click(submitRating);
            const userMovieList = JSON.parse(localStorage.getItem("userMovieList"))
            for (let listName in userMovieList) {
                $('#selected-list-options').append(`
                    <label><input type="checkbox" name="selected-lists" value="${listName}">${listName}</label><br>
                `);
            }
            $('#movie-info2').html(`
                <div id="ratingResult"></div>
                <h2>Gender Distribution</h2>
                <div class="chart-container">
                    <div class="chart" id="genderPieChart"></div>
                    <div class="chart" id="genderBarChart"></div>
                </div>
                <h2>Age Distribution</h2>
                <div class="chart-container">
                    <div class="chart" id="agePieChart"></div>
                    <div class="chart" id="ageBarChart"></div>
                </div>
                <h2>Occupation Distribution</h2>
                <div class="chart-container">
                    <div class="chart" id="occupationPieChart"></div>
                    <div class="chart" id="occupationBarChart"></div>
                </div>
            `);

            getCurrentRating();

            const genderData = [
                {label: "Male", value: movie.genderRatio[0]},
                {label: "Female", value: movie.genderRatio[1]}
            ];
            drawPieChart(genderData, "#genderPieChart");
            drawBarChart(genderData, "#genderBarChart");

            const ageLabels = ["Under 18", "18-24", "25-34", "35-44", "45-49", "50-55", "56+"]
            const ageData = movie.ageRatio.map((value, index) => ({
                label: ageLabels[index],
                value: value
            }));
            drawPieChart(ageData, "#agePieChart");
            drawBarChart(ageData, "#ageBarChart");

            const occupationLabels = [
                "Other or not specified",
                "Academic/Educator",
                "Artist",
                "Clerical/Admin",
                "College/Grad Student",
                "Customer Service",
                "Doctor/Health Care",
                "Executive/Managerial",
                "Farmer",
                "Homemaker",
                "K-12 Student",
                "Lawyer",
                "Programmer",
                "Retired",
                "Sales/Marketing",
                "Scientist",
                "Self-Employed",
                "Technician/Engineer",
                "Tradesman/Craftsman",
                "Unemployed",
                "Writer"
            ];
            const occupationData = movie.occupationRatio.map((value, index) => ({
                label: occupationLabels[index],
                value: value
            }));
            drawPieChart(occupationData, "#occupationPieChart");
            drawBarChart(occupationData, "#occupationBarChart");

            $('#add-button').click(performAdd);
        },
        error: function() {
            $('#movie-info').html('<p>Failed to bring in the movie.</p>');
        }
    });
});