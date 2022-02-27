package day02;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MoviesRatingsService {
    private MovieRepository movieRepository;
    private RatingsRepository ratingsRepository;

    public MoviesRatingsService(MovieRepository movieRepository, RatingsRepository ratingsRepository) {
        this.movieRepository = movieRepository;
        this.ratingsRepository = ratingsRepository;
    }
    public void addRatings(String title,Long... ratings){
        Optional<Movie> movie=movieRepository.findMovieByTitle(title);
        if(movie.isPresent()){
            ratingsRepository.insertRating(movie.get().getId(), Arrays.asList(ratings));
            List<Long> oldRatings=movieRepository.findMovieAllRatings(movie.get().getId());
            Double avgRating=oldRatings.stream().mapToDouble(d->d).average().orElse(0.0);
            movieRepository.insertAvgRating(avgRating,title);
        }else{
            throw new IllegalArgumentException("Cannot find movie: "+title);
        }
    }



}
