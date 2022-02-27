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

        Flyway flyway= Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();


        MovieRepository movieRepository= new MovieRepository(dataSource);
        ActorRepository actorRepository = new ActorRepository(dataSource);
        ActorsMoviesRepository  actorsMoviesRepository=new ActorsMoviesRepository(dataSource);

        ActorsMoviesService service = new ActorsMoviesService(actorRepository,movieRepository,actorsMoviesRepository);

        service.insertMoviesWithActors("Titanic",LocalDate.of(1997,12,11), List.of("Leonardo DiCaprio","Kate Winslet"));

        System.out.println(movieRepository.findAllMovies());





    }
}
