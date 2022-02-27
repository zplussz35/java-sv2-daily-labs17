package jdbcTemplate;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        MariaDbDataSource dataSource =new MariaDbDataSource();
        try{
            dataSource.setUrl("jdbc:mariadb://localhost:3306/bookstore?useUnicode=true");
            dataSource.setUserName("root");
            dataSource.setPassword("root");
        }catch (SQLException sqle){
            throw new IllegalStateException("Cannot reach database!",sqle);
        }

        Flyway flyway= Flyway.configure().locations("db/migration/bookstore").dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        BookRepository bookRepository=new BookRepository(dataSource);
        bookRepository.insertBook("Jókai Mór","Aranyember",5000,12);
        bookRepository.insertBook("Móricz Zsigmond","Kincskereső Kisködmön",7400,4);
        bookRepository.insertBook("Mikszáth Kálmán","Szent Péter esernyője",3500,7);
        bookRepository.insertBook("Mikszáth Kálmán","Tót atyafiak",3500,7);
        bookRepository.insertBook("Mikszáth Kálmán","A jó palócok",3500,7);
        bookRepository.insertBook("Mikszáth Kálmán","Beszterce ostroma",3500,7);
        bookRepository.insertBook("Madách Imre","Az ember tragédiája",2200,24);

        System.out.println(bookRepository.findBooksByWriter("Miksz"));
        bookRepository.updatePieces(4L,100);
        System.out.println(bookRepository.findBooksByWriter("M"));



    }
}
