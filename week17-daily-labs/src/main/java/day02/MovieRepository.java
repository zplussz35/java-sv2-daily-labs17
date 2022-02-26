package day02;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    private DataSource dataSource;

    public MovieRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveMovie(String title, LocalDate localDate){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("insert into movie(title,release_date) values(?,?)")){
            stmt.setString(1,title);
            stmt.setDate(2, Date.valueOf(localDate));
            stmt.executeUpdate();
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
}
