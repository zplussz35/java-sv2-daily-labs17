package activitytracker;

import org.mariadb.jdbc.MariaDbDataSource;


import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityTrackerMain {
    public static void main(String[] args) {
        Activity a1 = new Activity(1,LocalDateTime.of(2022,2,22,12,22),"descripiton:basketball", Type.BASKETBALL);
        Activity a2 = new Activity(2,LocalDateTime.of(2022,2,22,13,22),"descripiton:biking", Type.BIKING);
        Activity a3 = new Activity(3,LocalDateTime.of(2022,2,22,14,22),"descripiton:hiking", Type.HIKING);
        Activity a4 = new Activity(4,LocalDateTime.of(2022,2,22,15,22),"descripiton:running", Type.RUNNING);

        List<Activity> activities=new ArrayList<>();

        try {
            MariaDbDataSource dataSource = new MariaDbDataSource();
            dataSource.setUrl("jdbc:mariadb://localhost:3306/activitytracker?useUnicode=true");
            dataSource.setUserName("root");
            dataSource.setPassword("root");


            insertActivity(dataSource,a1);
            insertActivity(dataSource,a2);
            insertActivity(dataSource,a3);
            insertActivity(dataSource,a4);

            for (int i=1;i<5;i++){
                activities.add(selectActivityById(dataSource,i));
            }
            System.out.println(activities);




        } catch (SQLException se) {
            throw new IllegalStateException("Can not create data source", se);
        }


    }
    public static void insertActivity(DataSource dataSource, Activity a){
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("insert into activities(start_time,description,activity_type) values (?,?,?)");
        ) {
            stmt.setTimestamp(1,Timestamp.valueOf(a.getStartTime()));
            stmt.setString(2, a.getDescription());
            stmt.setString(3,a.getType().toString());
            stmt.execute();
        } catch (SQLException sqle) {
            throw new IllegalArgumentException("Error by insert", sqle);
        }
    }

    public static Activity selectActivityById(DataSource ds,int id){
        try(Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement("select * from activities where id=?")){
            ps.setInt(1,id);
            return selectActivityByPreparedStatement(ps);
        }catch (SQLException se){
            throw new IllegalStateException("Select error!",se);
        }
    }

    private static Activity selectActivityByPreparedStatement(PreparedStatement ps) {
        try(ResultSet rs = ps.executeQuery()){
            if(rs.next()){
                int id=rs.getInt("id");
                LocalDateTime startTime=rs.getTimestamp("start_time").toLocalDateTime();
                String description = rs.getString("description");
                String type = rs.getString("activity_type");
                Type realType = switch (type) {
                    case "BIKING" -> Type.BIKING;
                    case "HIKING" -> Type.HIKING;
                    case "RUNNING" -> Type.RUNNING;
                    case "BASKETBALL" -> Type.BASKETBALL;
                    default -> throw new IllegalStateException("Invalid activity type!");
                };
                Activity activity= new Activity(id,startTime,description,realType);

                return activity;
            }
            throw new IllegalStateException("Did not found!");
        }catch (SQLException se){
            throw new IllegalStateException("Cannot select!",se);
        }

    }


}
