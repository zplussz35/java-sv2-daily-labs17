package day02;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;

public class MovieMain {
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

        flyway.migrate();


        MovieRepository movieRepository= new MovieRepository(dataSource);

        movieRepository.saveMovie("Titanic", LocalDate.of(1997,12,11));
        movieRepository.saveMovie("Pokember", LocalDate.of(2007,12,11));
        movieRepository.saveMovie("Zombie", LocalDate.of(2018,12,11));

        System.out.println(movieRepository.findAllMovies());





    }
}
