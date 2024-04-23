package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping(value = "/")
public class Controller {
    @Autowired
    private MongoTemplate mongoTemplate;
    private  final MovieRepository movieRepository;
    private  final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final MovieDAL movieDAL;
    private final RatingDAL ratingDAL;
    private final UserDAL userDAL;
    private final EmployeeDAL employeeDAL;

    public Controller(MovieRepository movieRepository, RatingRepository ratingRepository, UserRepository userRepository, EmployeeRepository employeeRepository, MovieDAL movieDAL, RatingDAL ratingDAL, UserDAL userDAL, EmployeeDAL employeeDAL) {
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.movieDAL = movieDAL;
        this.ratingDAL = ratingDAL;
        this.userDAL = userDAL;
        this.employeeDAL = employeeDAL;
    }
    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }
    @RequestMapping(value = "/employees/{id}", method = RequestMethod.GET)
    public Employee getEmployee(@PathVariable String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invalid Id"));
    }
    @RequestMapping(value = "/movies/{movieId}", method = RequestMethod.GET)
    public Movie getMovie(@PathVariable String movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Invalid Id"));
    }
    @RequestMapping(value = "/ratings/{userId}/{movieId}", method = RequestMethod.GET)
    public Rating getRating(@PathVariable String userId, @PathVariable String movieId) {
        if (ratingDAL.checkUserIdAndMovieIdExist(userId, movieId))
            return ratingDAL.getRating(userId, movieId);
        else
            throw new RuntimeException("Invalid Ids");
    }
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public User getUser(@PathVariable String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Invalid Id"));
    }
    @RequestMapping(value = "/movies", method = RequestMethod.POST)
    public Movie addNewMovie(@RequestBody Movie movie) {
        if (movieDAL.checkMovieIdExists(movie.getMovieId()))
            throw new InputMismatchException("Invalid Id");
        else
            return movieRepository.save(movie);
    }
    @RequestMapping(value = "/employees", method = RequestMethod.POST)
    public Employee addNewEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee.inc += 1;
        Employee employee = new Employee(Employee.inc.toString(), employeeDTO.getName(), employeeDTO.getRole());
        return employeeRepository.save(employee);
    }
    @RequestMapping(value = "/ratings", method = RequestMethod.POST)
    public Rating addNewRating(@RequestBody Rating rating) {
        if (rating.getRating() >= 1 && rating.getRating() <= 5) {
            if (ratingDAL.checkUserIdAndMovieIdExist(rating.getUserId(), rating.getMovieId()) ||
                    !userDAL.checkUserIdExists(rating.getUserId()) ||
                    !movieDAL.checkMovieIdExists(rating.getMovieId()))
                throw new RuntimeException("Invalid Ids");
            else
                return ratingRepository.save(rating);
        }
        else
            throw new RuntimeException("Invalid rating range");
    }
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User addNewUser(@RequestBody User user) {
        if (userDAL.checkUserIdExists(user.getUserId()))
            throw new InputMismatchException("Invalid Id");
        else
            return userRepository.save(user);
    }
    @RequestMapping(value = "/employees/{id}", method = RequestMethod.PUT)
    public Employee updateEmployee(@RequestBody Employee employee, @PathVariable String id) {
        if (employeeDAL.checkEmployeeIdExists(id))
            return employeeRepository.save(employee);
        else
            throw new RuntimeException("Invalid Id");
    }
    @RequestMapping(value = "/movies/{movieId}", method = RequestMethod.PUT)
    public Movie updateMovie(@RequestBody Movie movie, @PathVariable String movieId) {
        if (movieDAL.checkMovieIdExists(movieId))
            return movieRepository.save(movie);
        else
            throw new RuntimeException("Invalid Id");
    }
    @RequestMapping(value = "/ratings/{userId}/{movieId}", method = RequestMethod.PUT)
    public Rating updateRating(@RequestBody String rating, @PathVariable String userId, @PathVariable String movieId) {
        double doubleRating = Double.parseDouble(rating);
        if (doubleRating >= 1 && doubleRating <= 5) {
            if (ratingDAL.checkUserIdAndMovieIdExist(userId, movieId)) {
                String id = ratingDAL.getIdByUserIdAndMovieId(userId, movieId);
                Rating updatingRating = ratingRepository.findById(id).get();
                updatingRating.setRating(doubleRating);
                updatingRating.setTimeStamp(String.valueOf(Instant.now().getEpochSecond()));
                return updatingRating;
            }
            else
                throw new RuntimeException("Invalid Ids");
        }
        else
            throw new InputMismatchException("Invalid rating range");
    }
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
    public User updateUser(@RequestBody User user, @PathVariable String userId) {
        if (userDAL.checkUserIdExists(userId) && Objects.equals(user.getUserId(), userId))
            return userRepository.save(user);
        else
            throw new RuntimeException("Invalid Id");
    }
    @RequestMapping(value = "/ratings/{rating}", method = RequestMethod.GET)
    public List<Movie> getMoviesByRating(@PathVariable String rating) {
        double doubleRating = Double.parseDouble(rating);
        if (doubleRating >= 1 && doubleRating <= 5)
            return movieDAL.getMovieInfosByMovieId(ratingDAL.getMovieIdsByRating(doubleRating));
        else
            throw new InputMismatchException("Invalid rating range");

    }

    @RequestMapping(value = "/recommendation/movie/{movieId}", method = RequestMethod.GET)
    public List<Movie> getRecommendationByMovieId(@PathVariable String MovieId) {
        return null;
    }

    @RequestMapping(value = "/recommendation/season", method = RequestMethod.GET)
    public List<Movie> getRecommendationBySeason() {
        LocalDate today = LocalDate.now();
        int monthNumber = today.getMonthValue();
        List<Rating> ratings;
        if (monthNumber < 3 || monthNumber == 12)
            return movieDAL.getMovieInfosByMovieId(ratingRepository.getRecommendationWinter());
        else if (monthNumber < 6)
            return movieDAL.getMovieInfosByMovieId(ratingRepository.getRecommendationSpring());
        else if (monthNumber < 9)
            return movieDAL.getMovieInfosByMovieId(ratingRepository.getRecommendationSummer());
        else
            return movieDAL.getMovieInfosByMovieId(ratingRepository.getRecommendationFall());
    }
}
