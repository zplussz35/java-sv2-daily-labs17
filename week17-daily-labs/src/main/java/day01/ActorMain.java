package day01;

import org.mariadb.jdbc.MariaDbDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActorMain {
    public static void main(String[] args) {
        List<Actor> actors = new ArrayList<>(Arrays.asList(new Actor("Zoltán"),
        new Actor("Mária"),
        new Actor("László"),
        new Actor("Sándor")));

        try{
            MariaDbDataSource dataSource= new MariaDbDataSource();
            dataSource.setUrl("jdbc:mariadb://localhost:3306/actor?useUnicode=true");
            dataSource.setUserName("root");
            dataSource.setPassword("root");

            for (Actor a:actors) {
                insertActor(dataSource,a);
            }
            for (int i = 1; i < 5; i++) {
                System.out.println(selectActorWithId(dataSource,i));
            }

        }catch (SQLException se){
            throw new IllegalStateException("Can not open data source!",se);
        }


    }
    private static Actor selectActorWithId(DataSource dataSource,int id){
        try(Connection conn=dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from actors where id=?")){
            ps.setInt(1,id);
            return selectActorWithPreparedStatement(ps);

        }catch (SQLException se){
            throw new IllegalStateException("Cannot select!",se);
        }
    }

    private static Actor selectActorWithPreparedStatement(PreparedStatement ps) {
        try(ResultSet rs= ps.executeQuery()){
            if(rs.next()){
                int id=rs.getInt(1);
                String name=rs.getString(2);
                return new Actor(id,name);
            }
            throw new IllegalStateException("Don't found!");

        }catch (SQLException se){
            throw new IllegalStateException("Cannot make prepared statement!",se);
        }
    }

    private static void insertActor(MariaDbDataSource dataSource, Actor a) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("insert into actors (actor_name) values (?)")){
            ps.setString(1,a.getActorName());
            ps.executeUpdate();
        }catch (SQLException se){
            throw new IllegalStateException("Insert error!",se);
        }
    }
}
