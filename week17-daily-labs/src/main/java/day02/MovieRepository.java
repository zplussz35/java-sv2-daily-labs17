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
                Movie movie = new Movie(rs.getLong(1),rs.getString(2),rs.getDate(3).toLocalDate());
                movies.add(movie);
            }
            return movies;

        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot find any!",sqle);
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
}
