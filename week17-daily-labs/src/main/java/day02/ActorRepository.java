package day02;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActorRepository {

    private DataSource dataSource;

    public ActorRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long saveActor(String name){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("insert into actor(name) values (?)",
                    Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,name);
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getLong(1);
                }
                throw new IllegalStateException("Cannot insert into actors!");
            }
        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot reach database!");
        }
    }

    public List<Actor> findAllActors(){
        List<Actor> actors = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
        Statement stmt=conn.createStatement();
            ResultSet rs =stmt.executeQuery("select * from actor")){
            while(rs.next()){
                actors.add(new Actor(rs.getLong("id"),rs.getString("name")));
            }
            return actors;

        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot select!");
        }
    }

    public Optional<Actor> findActorByName(String name){
        try(Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from actor where name=?")){
            ps.setString(1,name);

            return getActor(ps);

        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot connect to database!",sqle);
        }
    }

    private Optional<Actor> getActor(PreparedStatement ps) {
        try(ResultSet rs = ps.executeQuery()){

            if(rs.next()){
                return Optional.of(new Actor(rs.getLong("id"),rs.getString("name")));
            }
            return Optional.empty();

        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot select actor!",sqle);
        }
    }

    public List<String> findActorsWithPrefix(String prefix) {
        List<String> actorsNames=new ArrayList<>();

        try(Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("select name from actor where name like ?")){
            ps.setString(1,prefix + "%");
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    actorsNames.add(rs.getString("name"));
                }

            }
        }catch (SQLException sqle){
            throw  new IllegalStateException("Cannot connect to database!",sqle);
        }
        return actorsNames;
    }
}
