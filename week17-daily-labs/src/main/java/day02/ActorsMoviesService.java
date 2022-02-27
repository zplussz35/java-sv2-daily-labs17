package day02;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ActorsMoviesService {
    private ActorRepository actorRepository;
    private MovieRepository movieRepository;
    private ActorsMoviesRepository actorsMoviesRepository;

    public ActorsMoviesService(ActorRepository actorRepository, MovieRepository movieRepository, ActorsMoviesRepository actorsMoviesRepository) {
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
        this.actorsMoviesRepository = actorsMoviesRepository;
    }

    public void insertMoviesWithActors(String title, LocalDate releaseDate, List<String> actors){
        Long movieId=movieRepository.saveMovie(title,releaseDate);
        Long actorId;

        for (String name:actors) {
            Optional<Actor> found=actorRepository.findActorByName(name);
            if(found.isPresent()){
                actorId=found.get().getId();
            }else{
                actorId=actorRepository.saveActor(name);
            }
            actorsMoviesRepository.insertActorAndMovieId(actorId,movieId);
        }
    }


}
