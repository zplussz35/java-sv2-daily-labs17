package day02;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ActorsMoviesRepository {

    private DataSource dataSource;

    public ActorsMoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertActorAndMovieId(Long actorId,Long movieId){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("insert into actors_movies(actor_id,movie_id) values(?,?) ")){
            ps.setLong(1,actorId);
            ps.setLong(2,movieId);
            ps.executeUpdate();
        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot insert!",sqle);
        }
    }
}
