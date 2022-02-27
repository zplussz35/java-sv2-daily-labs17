package day02;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieRepository {

    private DataSource dataSource;

    public MovieRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long saveMovie(String title, LocalDate localDate){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("insert into movie(title,release_date) values(?,?)",
                    Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1,title);
            stmt.setDate(2, Date.valueOf(localDate));
            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getLong(1);
                }
                throw new IllegalStateException("insert failed to movies!");
            }
        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot connect!",sqle);
        }
    }

    public List<Movie> findAllMovies(){
        List<Movie> movies=new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from movie")){
            while(rs.next()){
                Movie movie = new Movie(rs.getLong("id"),rs.getString("title"),rs.getDate("release_date").toLocalDate());
                movies.add(movie);
            }
            return movies;

        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot find any!",sqle);
        }
    }

    public Optional<Movie> findMovieById(Long id){
        try(Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from movie where id=?")){
            ps.setLong(1,id);
            return processQuery(ps);
        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot connect to movies!",sqle);
        }
    }

    private Optional<Movie> processQuery(PreparedStatement ps) {
        try(ResultSet rs = ps.executeQuery()){
            if(rs.next()){
                return Optional.of(new Movie(rs.getLong("id"), rs.getString("title"),rs.getDate("release_date").toLocalDate()));
            }
            return Optional.empty();
        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot query from movies!",sqle);
        }
    }

    public Optional<Movie> findMovieByTitle(String title){
        try(Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from movie where title=?")){
            ps.setString(1,title);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return Optional.of(new Movie(rs.getLong("id"),rs.getString("title"),rs.getDate("release_date").toLocalDate()));
                }
                return Optional.empty();
            }

        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot connect to movies!",sqle);
        }
    }

    public List<Long> findMovieAllRatings(Long movieId){
        try(Connection conn = dataSource.getConnection();
        PreparedStatement stmt =conn.prepareStatement("select rating from ratings where movie_id=?")){
            stmt.setLong(1,movieId);
            return execQuery(stmt);
        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot connect to movies!",sqle);
        }
    }

    private List<Long> execQuery(PreparedStatement stmt) {
        List<Long> ratings = new ArrayList<>();
        try(ResultSet rs = stmt.executeQuery()){
            while(rs.next()){
                ratings.add(rs.getLong("rating"));
            }

        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot query from movie!",sqle);
        }
        return ratings;
    }

    public void insertAvgRating(Double rating,String title){
        try(Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("update movie set avg_rating=? where title=?")){
            ps.setDouble(1,rating);
            ps.setString(2,title);
            ps.executeUpdate();
        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot connect to movies",sqle);
        }
    }


}
