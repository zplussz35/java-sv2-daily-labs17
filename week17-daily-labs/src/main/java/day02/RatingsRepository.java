package day02;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RatingsRepository {
    private DataSource dataSource;

    public RatingsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Long> insertRating(Long movieId, List<Long> ratings){
        try(Connection conn = dataSource.getConnection()){
            conn.setAutoCommit(false);
            try(PreparedStatement ps=conn.prepareStatement("insert into ratings(movie_id,rating) values (?,?)")){
                for (Long actual: ratings) {
                    if (actual < 1 || actual > 5) {
                        throw new IllegalArgumentException("Illegal rating value (1-5)!");
                    }
                    ps.setLong(1, movieId);
                    ps.setLong(2, actual);
                    ps.executeUpdate();
                }
                conn.commit();

            }catch (IllegalArgumentException ilae){
                conn.rollback();
            }
        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot connect to ratings!",sqle);
        }
        return ratings;
    }
}
