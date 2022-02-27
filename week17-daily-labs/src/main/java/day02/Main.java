package day02;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MariaDbDataSource dataSource =new MariaDbDataSource();
        try{
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUserName("root");
            dataSource.setPassword("root");
        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot reach database!",sqle);
        }

        Flyway flyway= Flyway.configure().locations("db/migration/movie").dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();


        MovieRepository movieRepository= new MovieRepository(dataSource);
        ActorRepository actorRepository = new ActorRepository(dataSource);
        ActorsMoviesRepository  actorsMoviesRepository=new ActorsMoviesRepository(dataSource);
        RatingsRepository ratingsRepository= new RatingsRepository(dataSource);

        ActorsMoviesService service = new ActorsMoviesService(actorRepository,movieRepository,actorsMoviesRepository);
        MoviesRatingsService moviesRatingsService=new MoviesRatingsService(movieRepository,ratingsRepository);

        service.insertMoviesWithActors("Pokember",LocalDate.of(1997,12,11), List.of("Leonardo DiCaprio","Gondor"));
        service.insertMoviesWithActors("Titanic",LocalDate.of(1997,12,11), List.of("Leonardo DiCaprio","Kate Winslet"));

        moviesRatingsService.addRatings("Titanic",1L,2L,4L,5L,2L,3L);
        moviesRatingsService.addRatings("Pokember",1L,2L,5L,5L,5L,5L,4L);
        System.out.println(movieRepository.findAllMovies());





    }
}
