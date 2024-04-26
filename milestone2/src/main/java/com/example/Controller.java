package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

@RestController
@RequestMapping(value = "/")
public class Controller {
    @Autowired
    private MongoTemplate mongoTemplate;
    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final MovieDAL movieDAL;
    private final RatingDAL ratingDAL;
    private final UserDAL userDAL;
    private final EmployeeDAL employeeDAL;
    private String currentUserID;
    public static List<UserMovies> associations;
    public static Map<String, Integer> totalCounter = new HashMap<>();

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
    public void getMovie(@PathVariable String movieId) {

        if(movieDAL.checkMovieIdExists(movieId)){
            String movieName=movieRepository.findById(movieId).orElseThrow(()->
                    new RuntimeException("Invalid ID")).getTitle();
            String movieJenre=movieRepository.findById(movieId).orElseThrow(()->
                    new RuntimeException("Invalid ID")).getGenre();
            System.out.println("moive-name: "+movieName);
            System.out.println("movie-jenre: "+movieJenre);


            AggregationResults<AverageRating> result = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.group("movieId")
                                    .first("movieId").as("movieId")
                                    .avg("rating").as("averageRating")),
                    "ratings",
                    AverageRating.class
            );
            List<String> listResultMovieId=result.getMappedResults().stream()
                    .map(AverageRating::getMovieId)
                    .collect(Collectors.toList());
            List<Double> listResultAverageRating=result.getMappedResults().stream()
                    .map(AverageRating::getAverageRating)
                    .collect(Collectors.toList());
            int index = listResultMovieId.indexOf(movieId);

            System.out.printf("AverageRating: %.3f\n", listResultAverageRating.get(index));


            List<Rating> userIdbyRating = ratingDAL.getUserbyMovieId(movieId);

            float[] GenderRatio=new float[2];
            float[] AgeRatio=new float[7];
            float[] OccupationRatio=new float[21];

            for(Rating innerRating: userIdbyRating){
                User user=userDAL.getUserbyUserId(innerRating.getUserId());
                if(user.getGender().equals("M")){
                    GenderRatio[0]+=1;
                }else{
                    GenderRatio[1]+=1;
                }
                if(userDAL.classifyAge(user.getAge()).equals(-1)){
                    throw new RuntimeException(("Invalid Age"));
                }else{
                    AgeRatio[userDAL.classifyAge((user.getAge()))]+=1;
                }
                if(userDAL.classifyOccupation(user.getOccupation()).equals(-1)){
                    throw new RuntimeException("Invalid Occupation");
                }else{
                    OccupationRatio[userDAL.classifyOccupation(user.getOccupation())]+=1;
                }
            }
            float sumOfPeople = GenderRatio[0]+GenderRatio[1];

            System.out.print("Ratio of Age->");
            System.out.printf("Under 18: %.3f ", AgeRatio[0]/sumOfPeople);
            System.out.printf("18~24: %.3f ", AgeRatio[1]/sumOfPeople);
            System.out.printf("25~34: %.3f ", AgeRatio[2]/sumOfPeople);
            System.out.printf("35~44: %.3f ", AgeRatio[3]/sumOfPeople);
            System.out.printf("45~49: %.3f ", AgeRatio[4]/sumOfPeople);
            System.out.printf("50~55: %.3f ", AgeRatio[5]/sumOfPeople);
            System.out.printf("56+: %.3f\n", AgeRatio[6]/sumOfPeople);

            System.out.println("Ratio of Occupation | \"other\" or not specified | " +
                    "academic/educator | artist | clerical/admin | college/grad student | " +
                    "customer service | doctor/health care | executive/managerial | farmer | " +
                    "homemaker | K-12 student | lawyer | programmer | retired | " +
                    "sales/marketing | scientist | self-employed | technician/engineer | " +
                    "tradesman/craftsman | unemployed | writer");
            System.out.printf("%.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f\n"
            ,OccupationRatio[0]/sumOfPeople, OccupationRatio[1]/sumOfPeople, OccupationRatio[2]/sumOfPeople, OccupationRatio[3]/sumOfPeople, OccupationRatio[4]/sumOfPeople, OccupationRatio[5]/sumOfPeople, OccupationRatio[6]/sumOfPeople, OccupationRatio[7]/sumOfPeople, OccupationRatio[8]/sumOfPeople, OccupationRatio[9]/sumOfPeople, OccupationRatio[10]/sumOfPeople, OccupationRatio[11]/sumOfPeople, OccupationRatio[12]/sumOfPeople, OccupationRatio[13]/sumOfPeople, OccupationRatio[14]/sumOfPeople, OccupationRatio[15]/sumOfPeople, OccupationRatio[16]/sumOfPeople, OccupationRatio[17]/sumOfPeople, OccupationRatio[18]/sumOfPeople, OccupationRatio[19]/sumOfPeople, OccupationRatio[20]/sumOfPeople);

            System.out.printf("Ratio of Gender->M: %.3f F: %.3f\n ",
                    GenderRatio[0]/sumOfPeople, GenderRatio[1]/sumOfPeople);
        }
        else{
            throw new RuntimeException("Invalid Id");
        }
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
    public List<Movie> getRecommendationByMovieId(@PathVariable String movieId) {
        HashMap<String, Integer> counter = new HashMap<>();
        int countMovieId = 0;
        for (UserMovies userMovies : associations) {
            if (userMovies.getMovieIds().contains(movieId)) {
                countMovieId++;
                for (String e : userMovies.getMovieIds()) {
                    counter.put(e, counter.getOrDefault(e, 0) + 1);
                }
            }
        }
        if (countMovieId < 10)
            return null;
        int finalCountMovieId = countMovieId;
        return movieDAL.getMovieInfosByMovieId(counter.entrySet().stream()
                .filter(entry -> entry.getValue() > 9)
                .sorted(Comparator.comparingDouble(
                        (Map.Entry<String, Integer> entry) -> (double) totalCounter.get(entry.getKey()) * finalCountMovieId / entry.getValue()
                ))
                .map(Map.Entry::getKey)
                .skip(1)
                .limit(20)
                .toList());
    }

    @RequestMapping(value = "/recommendation/info/{userId}", method = RequestMethod.GET)
    public List<Movie> getRecommendationByInfo(@PathVariable String userId) {
        if (userDAL.checkUserIdExists(userId)) {
            Optional<User> optUser = userRepository.findById(userId);
            User user = optUser.get();
            HashMap<String, Integer> counter = new HashMap<>();
            String age = user.getAge();
            String gender = user.getGender();
            String occupation = user.getOccupation();

            Map<String, User> usersMap = userRepository.findAll()
                    .stream()
                    .collect(Collectors.toMap(User::getUserId, Function.identity()));

            for (UserMovies userMovies : associations) {
                User comparingUser = usersMap.get(userMovies.getUserId());
                int score = 0;
                score += (Objects.equals(comparingUser.getAge(), age)) ? 7 : -1;
                score += (Objects.equals(comparingUser.getGender(), gender)) ? 2 : -1;
                score += (Objects.equals(comparingUser.getOccupation(), occupation)) ? 21 : -1;
                for (String e : userMovies.getMovieIds()) {
                    counter.put(e, counter.getOrDefault(e, 0) + score);
                }
            }
            return movieDAL.getMovieInfosByMovieId(counter.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .map(Map.Entry::getKey)
                    .limit(20)
                    .toList()
            );
        } else {
            throw new RuntimeException("Invalid Id");
        }
    }

    @RequestMapping(value = "/recommendation/season", method = RequestMethod.GET)
    public List<Movie> getRecommendationBySeason() {
        LocalDate today = LocalDate.now();
        int monthNumber = today.getMonthValue();
        if (monthNumber < 3 || monthNumber == 12)
            return movieDAL.getMovieInfosByMovieId(ratingRepository.getRecommendationWinter());
        else if (monthNumber < 6)
            return movieDAL.getMovieInfosByMovieId(ratingRepository.getRecommendationSpring());
        else if (monthNumber < 9)
            return movieDAL.getMovieInfosByMovieId(ratingRepository.getRecommendationSummer());
        else
            return movieDAL.getMovieInfosByMovieId(ratingRepository.getRecommendationFall());
    }

    @RequestMapping(value = "/recommendation/time", method = RequestMethod.GET)
    public List<Movie> getRecommendationByTime() {
        LocalTime now = LocalTime.now();
        int hourNumber = now.getHour();
        if (hourNumber <= 6)
            return movieDAL.getMovieInfosByMovieId(ratingRepository.getRecommendationDawn());
        else if (hourNumber <= 12)
            return movieDAL.getMovieInfosByMovieId(ratingRepository.getRecommendationMorning());
        else if (hourNumber <= 18)
            return movieDAL.getMovieInfosByMovieId(ratingRepository.getRecommendationAfternoon());
        else
            return movieDAL.getMovieInfosByMovieId(ratingRepository.getRecommendationNight());
    }

    // Bookmark function
    // Password match
    @RequestMapping(value = "/users/{userId}/{password}", method = RequestMethod.GET)
    public User passwordMatch(@PathVariable String userId, @PathVariable String password) {
        if (userDAL.checkUserIdExists(userId)) {
            Optional<User> optUser = userRepository.findById(userId);
            User user = optUser.get();

            if (Objects.equals(user.getPassword(), password)) {
                System.out.println("Log in successful with ID : " + userId);
                return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Invalid ID"));
            } else {
                throw new RuntimeException("Password do not match");
            }
        } else {
            throw new RuntimeException("Invalid Id");
        }
    }

    // Update password
    @RequestMapping(value = "/users/password/{userId}/{passwordOld}", method = RequestMethod.PUT)
    public User changeUserPassword(@RequestBody String passwordNew, @PathVariable String userId, @PathVariable String passwordOld) {
        if (userDAL.checkUserIdExists(userId)) {
            User user = userRepository.findById(userId).get();

            if (Objects.equals(user.getPassword(), passwordOld)) {
                User updateUser = userRepository.findById(userId).get();
                updateUser.setPassword(passwordNew);
                userRepository.save(updateUser);
                return updateUser;
            } else {
                throw new RuntimeException("Password do not match");
            }
        } else {
            throw new RuntimeException("Invalid Id");
        }
    }

    // Update info
    @RequestMapping(value = "/users/info/{userId}/{type}", method = RequestMethod.PUT)
    public User changeUserInfo(@RequestBody String value, @PathVariable String userId, @PathVariable String type) {
        if (userDAL.checkUserIdExists(userId)) {
            User updateUser = userRepository.findById(userId).get();

            if (type == "gender") {
                if (value == "M" || value == "F") {
                    updateUser.setGender(value);
                    userRepository.save(updateUser);
                    return updateUser;
                } else {
                    throw new RuntimeException("invalid value");
                }
            } else if (type == "age") {
                if (value == "1" || value == "18" || value == "25" || value == "35" || value == "45" || value == "50" || value == "56") {
                    updateUser.setAge(value);
                    userRepository.save(updateUser);
                    return updateUser;
                } else {
                    throw new RuntimeException("invalid value");
                }
            } else if (type == "occupation") {
                int occupation = Integer.parseInt(value);
                if (occupation >= 0 && occupation <= 20) {
                    updateUser.setOccupation(value);
                    userRepository.save(updateUser);
                    return updateUser;
                } else {
                    throw new RuntimeException("invalid value");
                }
            } else if (type == "username") {
                updateUser.setUserName(value);
                userRepository.save(updateUser);
                return updateUser;
            } else {
                throw new RuntimeException("invalid operation");
            }
        } else {
            throw new RuntimeException("invalid user");
        }
    }

    // Setting movie list
    @RequestMapping(value = "/users/movielist/{userId}", method = RequestMethod.PUT)
    public User addMovieList(@RequestBody String listname, @PathVariable String userId) {
        if (userDAL.checkUserIdExists(userId)) {
            User updateUser = userRepository.findById(userId).get();
            HashMap<String, List<String>> updateList = updateUser.getMovieList();
            updateList.put(listname, new ArrayList<String>());
            userRepository.save(updateUser);
            return updateUser;
        } else {
            throw new RuntimeException("invalid user");
        }
    }

    @RequestMapping(value = "users/movielist/{userId}/{listname}", method = RequestMethod.PUT)
    public User addElementToMovieList(@RequestBody String element, @PathVariable String userId, @PathVariable String listname) {
        if (userDAL.checkUserIdExists(userId)) {
            User updateUser = userRepository.findById(userId).get();
            HashMap<String, List<String>> updateList = updateUser.getMovieList();
            List<String> tempList = updateList.get(listname);
            tempList.add(element);
            userRepository.save(updateUser);
            return updateUser;
        } else {
            throw new RuntimeException("invalid user");
        }
    }

    @RequestMapping(value = "/users/movielist/{userId}/{listname}/{index}", method = RequestMethod.PUT)
    public User removeElementFromMovieList(@PathVariable String userId, @PathVariable String listname, @PathVariable String index) {
        if (userDAL.checkUserIdExists(userId)) {
            int intIndex = Integer.parseInt(index);
            User updateUser = userRepository.findById(userId).get();
            HashMap<String, List<String>> updateList = updateUser.getMovieList();
            List<String> tempList = updateList.get(listname);
            tempList.remove(intIndex);
            userRepository.save(updateUser);
            return updateUser;
        } else {
            throw new RuntimeException("invalid user");
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<Movie> getSearch(@RequestBody String line) {
        return null;
    }
}
